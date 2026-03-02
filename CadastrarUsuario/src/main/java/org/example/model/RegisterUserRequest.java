package org.example.model;

import java.time.LocalDate;

public record RegisterUserRequest(
        UserType type,
        String fullName,
        String gender,
        String rg,
        String cpf,
        String phone,
        String address,
        String digitalWallet,  // opcional (RF001)
        String healthPlan,     // opcional (RF001)
        LocalDate birthDate,
        String email,
        String password,
        String crm,            // obrigatório apenas para médico
        DocumentPhoto documentPhoto
) { }