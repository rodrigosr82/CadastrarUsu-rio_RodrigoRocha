package org.example.controller;

import org.example.model.CreatePrescriptionRequest;
import org.example.model.Prescription;
import org.example.service.PrescriptionService;

import java.util.Objects;
import java.util.UUID;

public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = Objects.requireNonNull(prescriptionService);
    }

    public Prescription create(UUID doctorId, CreatePrescriptionRequest request) {
        return prescriptionService.create(doctorId, request);
    }
}