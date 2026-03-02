package org.example.model;

import java.time.LocalDate;
import java.util.UUID;

public class User {
    private final UUID id;
    private final UserType type;
    private final String fullName;
    private final String gender;
    private final String rg;
    private final String cpf;
    private final String phone;
    private final String address;
    private final String digitalWallet;
    private final String healthPlan;
    private final LocalDate birthDate;
    private final String email;
    private final String password;
    private final String crm;
    private final DocumentPhoto documentPhoto;

    public User(UUID id, UserType type, String fullName, String gender, String rg, String cpf,
                String phone, String address, String digitalWallet, String healthPlan,
                LocalDate birthDate, String email, String password, String crm,
                DocumentPhoto documentPhoto) {
        this.id = id;
        this.type = type;
        this.fullName = fullName;
        this.gender = gender;
        this.rg = rg;
        this.cpf = cpf;
        this.phone = phone;
        this.address = address;
        this.digitalWallet = digitalWallet;
        this.healthPlan = healthPlan;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.crm = crm;
        this.documentPhoto = documentPhoto;
    }

    public UUID getId() { return id; }
    public UserType getType() { return type; }
    public String getFullName() { return fullName; }
    public String getGender() { return gender; }
    public String getRg() { return rg; }
    public String getCpf() { return cpf; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getDigitalWallet() { return digitalWallet; }
    public String getHealthPlan() { return healthPlan; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getCrm() { return crm; }
    public DocumentPhoto getDocumentPhoto() { return documentPhoto; }
}