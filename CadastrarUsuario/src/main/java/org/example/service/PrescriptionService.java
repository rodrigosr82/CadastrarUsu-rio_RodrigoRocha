package org.example.service;

import org.example.model.CreatePrescriptionRequest;
import org.example.model.Prescription;
import org.example.model.User;
import org.example.model.UserType;
import org.example.repository.PrescriptionRepository;
import org.example.repository.UserRepository;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class PrescriptionService {

    private final UserRepository userRepository;
    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(UserRepository userRepository,
                              PrescriptionRepository prescriptionRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.prescriptionRepository = Objects.requireNonNull(prescriptionRepository);
    }

    public Prescription create(UUID doctorId, CreatePrescriptionRequest req) {
        if (doctorId == null) throw new ValidationException("doctorId não pode ser nulo.");
        if (req == null) throw new ValidationException("Requisição de prescrição não pode ser nula.");

        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new ValidationException("Médico não autenticado ou inexistente."));

        if (doctor.getType() != UserType.MEDICO) {
            throw new ValidationException("Apenas médico pode cadastrar prescrição.");
        }

        requireNotBlank(req.patientFullName(), "nome completo do paciente");
        requireNotBlank(req.doctorResponsible(), "médico responsável");
        requireNotBlank(req.doctorCode(), "código do médico");
        requireNotBlank(req.doctorFullName(), "nome completo do médico");
        requireNotNull(req.prescriptionDate(), "data da prescrição");
        requireNotBlank(req.medicationName(), "nome do medicamento");
        requireNotBlank(req.dosage(), "dosagem");
        requireNotBlank(req.posology(), "posologia");
        requireNotBlank(req.digitalSignature(), "assinatura digital");

        if (req.prescriptionDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Data da prescrição não pode estar no futuro.");
        }

        PasswordPolicy.validateOrThrow(req.accessPassword(), "senha de acesso");

        Prescription p = new Prescription(
                UUID.randomUUID(),
                doctorId,
                req.patientFullName().trim(),
                req.doctorResponsible().trim(),
                req.doctorCode().trim(),
                req.doctorFullName().trim(),
                req.prescriptionDate(),
                req.medicationName().trim(),
                req.dosage().trim(),
                req.posology().trim(),
                (req.additionalInfo() == null ? null : req.additionalInfo().trim()),
                req.digitalSignature().trim(),
                req.accessPassword()
        );

        return prescriptionRepository.save(p);
    }

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
}