package org.example;

import org.example.model.*;
import org.example.repository.InMemoryPrescriptionRepository;
import org.example.repository.InMemoryUserRepository;
import org.example.service.PrescriptionService;
import org.example.service.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class PrescriptionServiceTest {

    private InMemoryUserRepository userRepository;
    private InMemoryPrescriptionRepository prescriptionRepository;
    private PrescriptionService prescriptionService;

    private UUID doctorId;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        prescriptionRepository = new InMemoryPrescriptionRepository();
        prescriptionService = new PrescriptionService(userRepository, prescriptionRepository);

        // Pré-condição TC002: "usuário médico autenticado"
        User doctor = new User(
                UUID.randomUUID(),
                UserType.MEDICO,
                "João Paulo Mendonça",
                "masculino",
                "123",
                "39053344705",
                "(11) 99999-8888",
                "Rua X, 123",
                null,
                null,
                LocalDate.of(1980, 1, 1),
                "dr.joao@email.com",
                "Medico#2024",
                "CRM-SP-123456",
                new DocumentPhoto(PhotoQuality.NITIDA, "OK")
        );

        doctorId = doctor.getId();
        userRepository.save(doctor);
    }

    @Test
    void deveCadastrarPrescricaoComSucesso_quandoDadosForemValidos() {
        CreatePrescriptionRequest req = CreatePrescriptionRequestBuilder.aValidPrescription().build();

        Prescription saved = prescriptionService.create(doctorId, req);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDoctorId()).isEqualTo(doctorId);
        assertThat(saved.getMedicationName()).isEqualTo("Amoxicilina");
        assertThat(saved.getAccessPassword()).isEqualTo("Amox@500mg");
    }

    @Test
    void deveLancarExceptionQuandoMedicoNaoEstiverAutenticado() {
        CreatePrescriptionRequest req = CreatePrescriptionRequestBuilder.aValidPrescription().build();

        assertThatThrownBy(() -> prescriptionService.create(UUID.randomUUID(), req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("não autenticado");
    }

    @Test
    void deveLancarExceptionQuandoSenhaAcessoDaPrescricaoForInvalida() {
        CreatePrescriptionRequest req = CreatePrescriptionRequestBuilder.aValidPrescription()
                .withAccessPassword("senha") // inválida (TC002)
                .build();

        assertThatThrownBy(() -> prescriptionService.create(doctorId, req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("inválida");
    }

    @Test
    void deveLancarExceptionQuandoNomeMedicamentoForVazio() {
        CreatePrescriptionRequest req = CreatePrescriptionRequestBuilder.aValidPrescription()
                .withMedicationName(" ")
                .build();

        assertThatThrownBy(() -> prescriptionService.create(doctorId, req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("nome do medicamento");
    }

    @Test
    void deveLancarExceptionQuandoAssinaturaDigitalForVazia() {
        CreatePrescriptionRequest req = CreatePrescriptionRequestBuilder.aValidPrescription()
                .withDigitalSignature("")
                .build();

        assertThatThrownBy(() -> prescriptionService.create(doctorId, req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("assinatura digital");
    }

    // -------------------------
    // Test Data Builder (TDD-friendly)
    // -------------------------
    static class CreatePrescriptionRequestBuilder {
        private String patientFullName = "Ana Carolina Silva Santos";
        private String doctorResponsible = "Dr. João Paulo Mendonça";
        private String doctorCode = "CRM-SP-123456";
        private String doctorFullName = "João Paulo Mendonça";
        private LocalDate prescriptionDate = LocalDate.of(2024, 3, 15);
        private String medicationName = "Amoxicilina";
        private String dosage = "500mg";
        private String posology = "Tomar 1 comprimido de 8 em 8 horas por 7 dias";
        private String additionalInfo = "Tomar preferencialmente após as refeições.";
        private String digitalSignature = "ASSINATURA_DIGITAL_DR_JOAO";
        private String accessPassword = "Amox@500mg"; // atende política (8..16, maiúscula, número, especial)

        static CreatePrescriptionRequestBuilder aValidPrescription() {
            return new CreatePrescriptionRequestBuilder();
        }

        CreatePrescriptionRequestBuilder withMedicationName(String v) { this.medicationName = v; return this; }
        CreatePrescriptionRequestBuilder withDigitalSignature(String v) { this.digitalSignature = v; return this; }
        CreatePrescriptionRequestBuilder withAccessPassword(String v) { this.accessPassword = v; return this; }

        CreatePrescriptionRequest build() {
            return new CreatePrescriptionRequest(
                    patientFullName,
                    doctorResponsible,
                    doctorCode,
                    doctorFullName,
                    prescriptionDate,
                    medicationName,
                    dosage,
                    posology,
                    additionalInfo,
                    digitalSignature,
                    accessPassword
            );
        }
    }
}