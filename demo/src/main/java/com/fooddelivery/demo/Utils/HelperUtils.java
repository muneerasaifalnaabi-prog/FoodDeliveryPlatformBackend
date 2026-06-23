package com.fooddelivery.demo.Utils;

public class HelperUtils {

    public static String generateCode(String prefix) {
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        return prefix + "-" + randomNumber;
    }
    public static String generateCode(String prefix, int length) {

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = (int) (Math.random() * 10);
            code.append(digit);
        }
        return prefix + "-" + code;
    }
}
