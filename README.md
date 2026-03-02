# Testes-projeto-main
Projeto Java (Maven) com TDD para validar regras de negócio sem banco de dados real.

Rodar os testes
mvn test
Casos cobertos (arquivo Excel)
TC001 (cadastro usuário): TC001_01 ... TC001_16
TC002 (cadastro prescrição): cenários essenciais + política de senha
Regras (RF001)
Campos obrigatórios
Email único (case-insensitive)
CPF único + validação
Senha (8..16, maiúscula, número, especial)
Foto do documento obrigatória e deve estar NÍTIDA
Se for médico: CRM obrigatório e validado
