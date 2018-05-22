package com.example.ben.meallennium.utils;

public class FieldVerifier {

    public static boolean areVerifiedFields(String email, String password, String verifiedPass) {
        boolean basicVerification = areVerifiedFields(email, password);

        if(verifiedPass == null || verifiedPass.equals("")) {
            return basicVerification;
        }

        return basicVerification && password.equals(verifiedPass);
    }

    private static boolean areVerifiedFields(String email, String password) {
        if(!email.contains("@")) return false;
        if(password.length() < 6) return false;

        return true;
    }
}
