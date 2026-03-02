package org.example;

import org.example.model.*;
import org.example.repository.InMemoryUserRepository;
import org.example.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class UserServiceTest {

    private InMemoryUserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository);
    }

    // TC001 (fluxo - paciente)
    @Test
    void deveCadastrarPacienteComSucesso_quandoDadosForemValidos() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient().build();

        User saved = userService.register(req);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getType()).isEqualTo(UserType.PACIENTE);
        assertThat(saved.getEmail()).isEqualTo("carlos.lima@email.com");
        assertThat(saved.getCpf()).isEqualTo("04506474473"); // normalizado
    }

    // TC001 (fluxo - médico)
    @Test
    void deveCadastrarMedicoComSucesso_quandoCrmForValido() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidDoctor().build();

        User saved = userService.register(req);

        assertThat(saved.getType()).isEqualTo(UserType.MEDICO);
        assertThat(saved.getCrm()).isEqualTo("CRM-PR 78.901");
    }

    // TC001_01
    @Test
    void deveLancarExceptionQuandoTipoCadastroForVazio() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidDoctor()
                .withType(null)
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("tipo de cadastro");
    }

    // TC001_02
    @Test
    void deveLancarExceptionQuandoNomeDoUsuarioForVazio() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withFullName("   ")
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("nome completo");
    }

    // TC001_03
    @Test
    void deveLancarExceptionQuandoGeneroDoUsuarioForVazio() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withGender("")
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("gênero");
    }

    // TC001_04
    @Test
    void deveLancarExceptionQuandoRgDoUsuarioForVazio() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withRg(" ")
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("RG");
    }

    // TC001_05
    @Test
    void deveLancarExceptionQuandoCpfDoUsuarioForVazio() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withCpf(null)
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("CPF");
    }

    // Extra (fortalece regra de negócio)
    @Test
    void deveLancarExceptionQuandoCpfForInvalido() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withCpf("111.111.111-11")
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("CPF inválido");
    }

    // TC001_06
    @Test
    void deveLancarExceptionQuandoTelefoneDoUsuarioForVazio() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withPhone("   ")
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("telefone");
    }

    // TC001_07
    @Test
    void deveLancarExceptionQuandoEnderecoDoUsuarioForVazio() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withAddress("")
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("endereço");
    }

    // TC001_08
    @Test
    void deveLancarExceptionQuandoDataNascimentoDoUsuarioForVazia() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withBirthDate(null)
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("data de nascimento");
    }

    // TC001_09
    @Test
    void deveLancarExceptionQuandoEmailDoUsuarioForVazio() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withEmail(" ")
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("e-mail");
    }

    // TC001_10
    @Test
    void deveLancarExceptionQuandoSenhaDoUsuarioForVazia() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withPassword("")
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("senha");
    }

    // TC001_11
    @Test
    void deveLancarExceptionQuandoSenhaDoUsuarioForInvalida() {
        String[] invalidPasswords = {
                "senhafraca",              // sem maiúscula, sem número, sem especial
                "SENHA123",                // sem especial
                "Teste@1",                 // 7 caracteres (curta demais)
                "SenhaMuitoLonga@2024"     // longa demais
        };

        for (String pwd : invalidPasswords) {
            RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                    .withPassword(pwd)
                    .build();

            assertThatThrownBy(() -> userService.register(req))
                    .as("senha inválida: %s", pwd)
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("inválida");
        }
    }

    // TC001_12 
    @Test
    void deveLancarExceptionQuandoCrmDoUsuarioForVazio_quandoTipoForMedico() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidDoctor()
                .withCrm("   ")
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("CRM");
    }

    // TC001_13
    @Test
    void deveLancarExceptionQuandoEmailForDuplicado() {
        userService.register(RegisterUserRequestBuilder.aValidPatient().build());

        RegisterUserRequest req2 = RegisterUserRequestBuilder.aValidPatient()
                .withCpf("390.533.447-05") // CPF válido diferente
                .build();

        assertThatThrownBy(() -> userService.register(req2))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("E-mail já cadastrado");
    }

    // TC001_14
    @Test
    void deveLancarExceptionQuandoCpfForDuplicado() {
        userService.register(RegisterUserRequestBuilder.aValidPatient().build());

        RegisterUserRequest req2 = RegisterUserRequestBuilder.aValidPatient()
                .withEmail("outro@email.com")
                .build();

        assertThatThrownBy(() -> userService.register(req2))
                .isInstanceOf(DuplicateCpfException.class)
                .hasMessageContaining("CPF já cadastrado");
    }

    // TC001_15 
    @Test
    void deveLancarExceptionQuandoFotoDocumentoForVazia() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withDocumentPhoto(null)
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("foto do documento");
    }

    // TC001_16
    @Test
    void deveLancarExceptionQuandoFotoDocumentoNaoForNitida() {
        RegisterUserRequest req = RegisterUserRequestBuilder.aValidPatient()
                .withDocumentPhoto(new DocumentPhoto(PhotoQuality.DESFOCADA, "Sistema deve solicitar retomada"))
                .build();

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("não está nítida");
    }

    // -------------------------
    // Test Data Builder (TDD-friendly)
    // -------------------------
    static class RegisterUserRequestBuilder {
        private UserType type = UserType.PACIENTE;
        private String fullName = "Carlos Eduardo Lima";
        private String gender = "masculino";
        private String rg = "6773352";
        private String cpf = "045.064.744-73";
        private String phone = "(81) 98888-7777";
        private String address = "Av. Brasil, 456 - Centro, Rio de Janeiro - RJ, 20000-000";
        private String digitalWallet = null;
        private String healthPlan = null;
        private LocalDate birthDate = LocalDate.of(1990, 7, 22);
        private String email = "carlos.lima@email.com";
        private String password = "Carlos#2024";
        private String crm = null;
        private DocumentPhoto documentPhoto = new DocumentPhoto(PhotoQuality.NITIDA, "Documento Legível");

        static RegisterUserRequestBuilder aValidPatient() {
            return new RegisterUserRequestBuilder();
        }

        static RegisterUserRequestBuilder aValidDoctor() {
            return new RegisterUserRequestBuilder()
                    .withType(UserType.MEDICO)
                    .withCrm("CRM-PR 78.901");
        }

        RegisterUserRequestBuilder withType(UserType type) { this.type = type; return this; }
        RegisterUserRequestBuilder withFullName(String fullName) { this.fullName = fullName; return this; }
        RegisterUserRequestBuilder withGender(String gender) { this.gender = gender; return this; }
        RegisterUserRequestBuilder withRg(String rg) { this.rg = rg; return this; }
        RegisterUserRequestBuilder withCpf(String cpf) { this.cpf = cpf; return this; }
        RegisterUserRequestBuilder withPhone(String phone) { this.phone = phone; return this; }
        RegisterUserRequestBuilder withAddress(String address) { this.address = address; return this; }
        RegisterUserRequestBuilder withBirthDate(LocalDate birthDate) { this.birthDate = birthDate; return this; }
        RegisterUserRequestBuilder withEmail(String email) { this.email = email; return this; }
        RegisterUserRequestBuilder withPassword(String password) { this.password = password; return this; }
        RegisterUserRequestBuilder withCrm(String crm) { this.crm = crm; return this; }
        RegisterUserRequestBuilder withDocumentPhoto(DocumentPhoto documentPhoto) { this.documentPhoto = documentPhoto; return this; }

        RegisterUserRequest build() {
            return new RegisterUserRequest(
                    type, fullName, gender, rg, cpf, phone, address,
                    digitalWallet, healthPlan,
                    birthDate, email, password, crm, documentPhoto
            );
        }
    }

}
