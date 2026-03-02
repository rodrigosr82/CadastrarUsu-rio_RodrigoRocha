package org.example.repository;

import org.example.model.Prescription;

public interface PrescriptionRepository {
    Prescription save(Prescription prescription);
}