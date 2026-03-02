package org.example.model;

import java.time.LocalDate;
import java.util.UUID;

public class Prescription {
    private final UUID id;
    private final UUID doctorId;
    private final String patientFullName;
    private final String doctorResponsible;
    private final String doctorCode;
    private final String doctorFullName;
    private final LocalDate prescriptionDate;
    private final String medicationName;
    private final String dosage;
    private final String posology;
    private final String additionalInfo;
    private final String digitalSignature;
    private final String accessPassword;

    public Prescription(UUID id, UUID doctorId, String patientFullName, String doctorResponsible,
                        String doctorCode, String doctorFullName, LocalDate prescriptionDate,
                        String medicationName, String dosage, String posology, String additionalInfo,
                        String digitalSignature, String accessPassword) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientFullName = patientFullName;
        this.doctorResponsible = doctorResponsible;
        this.doctorCode = doctorCode;
        this.doctorFullName = doctorFullName;
        this.prescriptionDate = prescriptionDate;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.posology = posology;
        this.additionalInfo = additionalInfo;
        this.digitalSignature = digitalSignature;
        this.accessPassword = accessPassword;
    }

    public UUID getId() { return id; }
    public UUID getDoctorId() { return doctorId; }
    public String getPatientFullName() { return patientFullName; }
    public String getDoctorResponsible() { return doctorResponsible; }
    public String getDoctorCode() { return doctorCode; }
    public String getDoctorFullName() { return doctorFullName; }
    public LocalDate getPrescriptionDate() { return prescriptionDate; }
    public String getMedicationName() { return medicationName; }
    public String getDosage() { return dosage; }
    public String getPosology() { return posology; }
    public String getAdditionalInfo() { return additionalInfo; }
    public String getDigitalSignature() { return digitalSignature; }
    public String getAccessPassword() { return accessPassword; }
}