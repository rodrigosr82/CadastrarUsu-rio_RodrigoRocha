package org.example.model;

import java.time.LocalDate;

public record CreatePrescriptionRequest(
        String patientFullName,
        String doctorResponsible,
        String doctorCode,
        String doctorFullName,
        LocalDate prescriptionDate,
        String medicationName,
        String dosage,
        String posology,
        String additionalInfo,
        String digitalSignature,
        String accessPassword
) { }