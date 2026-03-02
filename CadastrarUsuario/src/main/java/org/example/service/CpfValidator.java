package org.example.service;

public final class CpfValidator {

    private CpfValidator() {}

    public static String normalize(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("\\D", "");
    }

    public static boolean isValid(String cpf) {
        String n = normalize(cpf);
        if (n == null || n.length() != 11) return false;

        // Rejeita CPFs com todos os dígitos iguais (111..., 000..., etc.)
        if (n.chars().distinct().count() == 1) return false;

        int d1 = digit(n, 9, 10);
        int d2 = digit(n, 10, 11);

        return d1 == Character.getNumericValue(n.charAt(9))
            && d2 == Character.getNumericValue(n.charAt(10));
    }

    private static int digit(String n, int len, int weightStart) {
        int sum = 0;
        for (int i = 0; i < len; i++) {
            int dig = Character.getNumericValue(n.charAt(i));
            sum += dig * (weightStart - i);
        }
        int mod = (sum * 10) % 11;
        return (mod == 10) ? 0 : mod;
    }
}