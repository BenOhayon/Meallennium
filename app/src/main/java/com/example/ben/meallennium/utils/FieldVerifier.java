package com.example.ben.meallennium.utils;

public class FieldVerifier {

    public static boolean verifyLogin(String email, String password) {
        return areVerifiedFields(email, password);
    }

    public static boolean verifyRegistration(String email, String password, String verifiedPass) {
        return areVerifiedFields(email, password, verifiedPass);
    }

    private static boolean areVerifiedFields(String email, String password, String verifiedPass) {
        boolean basicVerification = areVerifiedFields(email, password);

        return basicVerification
                && verifiedPass != null
                && !verifiedPass.equals("")
                && password.equals(verifiedPass);
    }

    private static boolean areVerifiedFields(String email, String password) {
        if(!email.contains("@")) return false;
        if(password.length() < 6) return false;

        return true;
    }
}
