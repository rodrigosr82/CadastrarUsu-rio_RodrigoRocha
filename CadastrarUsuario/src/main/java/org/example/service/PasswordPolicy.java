package org.example.service;

public final class PasswordPolicy {

    private PasswordPolicy() {}

    public static void validateOrThrow(String password, String fieldName) {
        if (password == null || password.isBlank()) {
            throw new ValidationException("O campo " + fieldName + " está vazio.");
        }

        int len = password.length();
        if (len < 8 || len > 16) {
            throw new ValidationException("A " + fieldName + " é inválida: deve ter entre 8 e 16 caracteres.");
        }

        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        if (!hasUpper || !hasDigit || !hasSpecial) {
            throw new ValidationException(
                    "A " + fieldName + " é inválida: deve conter pelo menos 1 letra maiúscula, 1 número e 1 caractere especial."
            );
        }
    }
}