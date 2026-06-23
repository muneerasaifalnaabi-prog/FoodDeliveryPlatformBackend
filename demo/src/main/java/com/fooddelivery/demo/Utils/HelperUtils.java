package com.fooddelivery.demo.Utils;

public class HelperUtils {

    public static String generateCode(String prefix) {
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        return prefix + "-" + randomNumber;
    }
}
