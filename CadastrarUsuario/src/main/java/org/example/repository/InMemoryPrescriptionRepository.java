package org.example.repository;

import org.example.model.Prescription;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryPrescriptionRepository implements PrescriptionRepository {

    private final Map<UUID, Prescription> storage = new HashMap<>();

    @Override
    public Prescription save(Prescription prescription) {
        storage.put(prescription.getId(), prescription);
        return prescription;
    }

    // útil em testes/depuração, se quiser
    public Prescription find(UUID id) {
        return storage.get(id);
    }
}