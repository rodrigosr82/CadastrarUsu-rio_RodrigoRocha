package org.example.service;

import org.example.model.*;
import org.example.repository.UserRepository;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    public User register(RegisterUserRequest req) {
        if (req == null) throw new ValidationException("Requisição de cadastro não pode ser nula.");

        if (req.type() == null) throw new ValidationException("O tipo de cadastro é obrigatório.");
        requireNotBlank(req.fullName(), "nome completo");
        requireNotBlank(req.gender(), "gênero");
        requireNotBlank(req.rg(), "RG");
        requireNotBlank(req.cpf(), "CPF");
        requireNotBlank(req.phone(), "telefone");
        requireNotBlank(req.address(), "endereço");
        requireNotNull(req.birthDate(), "data de nascimento");
        requireNotBlank(req.email(), "e-mail");

        String email = req.email().trim();
        if (!isEmailValid(email)) {
            throw new ValidationException("E-mail inválido.");
        }

        if (req.birthDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Data de nascimento não pode estar no futuro.");
        }

        String cpfNormalized = CpfValidator.normalize(req.cpf());
        if (!CpfValidator.isValid(cpfNormalized)) {
            throw new ValidationException("CPF inválido.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("E-mail já cadastrado.");
        }
        if (userRepository.existsByCpf(cpfNormalized)) {
            throw new DuplicateCpfException("CPF já cadastrado.");
        }

        PasswordPolicy.validateOrThrow(req.password(), "senha");

        if (req.documentPhoto() == null) {
            throw new ValidationException("O campo foto do documento está vazio.");
        }
        if (req.documentPhoto().getQuality() != PhotoQuality.NITIDA) {
            throw new ValidationException("A foto do documento não está nítida. Envie uma nova foto.");
        }

        if (req.type() == UserType.MEDICO) {
            requireNotBlank(req.crm(), "CRM");
            if (!isCrmValid(req.crm().trim())) {
                throw new ValidationException("CRM inválido.");
            }
        }

        User user = new User(
                UUID.randomUUID(),
                req.type(),
                req.fullName().trim(),
                req.gender().trim(),
                req.rg().trim(),
                cpfNormalized,
                req.phone().trim(),
                req.address().trim(),
                req.digitalWallet(), // opcional RF001
                req.healthPlan(),    // opcional RF001
                req.birthDate(),
                email,
                req.password(),
                req.crm() == null ? null : req.crm().trim(),
                req.documentPhoto()
        );

        return userRepository.save(user);
    }

    // ----------------- helpers -----------------

    private static void requireNotBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("O campo " + field + " está vazio.");
        }
    }

    private static void requireNotNull(Object value, String field) {
        if (value == null) {
            throw new ValidationException("O campo " + field + " está vazio.");
        }
    }

    // Email simples e suficiente pro seu cenário de testes
    private static boolean isEmailValid(String email) {
        if (email == null) return false;
        if (email.isBlank()) return false;
        if (email.contains(" ")) return false;

        int at = email.indexOf('@');
        if (at <= 0) return false;
        if (email.indexOf('@', at + 1) != -1) return false;

        int dot = email.lastIndexOf('.');
        return dot > at + 1 && dot < email.length() - 1;
    }

    // Aceita "CRM-PR 78.901" e "CRM-SP-123456"
    private static boolean isCrmValid(String crm) {
        if (crm == null) return false;
        if (!crm.startsWith("CRM-")) return false;
        if (crm.length() < 7) return false; // "CRM-XX?"

        char uf1 = crm.charAt(4);
        char uf2 = crm.charAt(5);
        if (!Character.isUpperCase(uf1) || !Character.isUpperCase(uf2)) return false;

        int i = 6;
        if (i < crm.length() && (crm.charAt(i) == ' ' || crm.charAt(i) == '-')) i++;

        // precisa ter pelo menos 1 dígito
        boolean hasDigit = false;
        for (; i < crm.length(); i++) {
            char c = crm.charAt(i);
            if (Character.isDigit(c)) { hasDigit = true; continue; }
            if (c == '.' || c == ',' || c == ' ') continue;
            return false;
        }
        return hasDigit;
    }

    private static String normalizeCpf(String cpf) {
        if (cpf == null) return "";
        StringBuilder sb = new StringBuilder(11);
        for (int i = 0; i < cpf.length(); i++) {
            char c = cpf.charAt(i);
            if (Character.isDigit(c)) sb.append(c);
        }
        return sb.toString();
    }
}