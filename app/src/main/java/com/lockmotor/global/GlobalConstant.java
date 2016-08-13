package com.lockmotor.global;

import java.util.Random;

/**
 * Created by trandinhdat on 7/26/16.
 */
public class GlobalConstant {
    public static final String DEVICE_PHONE_NUMBER_KEY = "DEVICE_PHONE_NUMBER";
    public static final String PASSWORD_KEY = "PASSWORD_KEY";
    public static final String PASSWORD_LENGTH_KEY = "PASSWORD_LENGTH";
    public static final String SHARE_PREFERENCES_NAME = "SHARE_PREFERENCES_NAME";
    public static final String CONTENT_UNLOCK = "UNLOCK";
    public static final String CONTENT_LOCK = "LOCK";
    public static final String CONTENT_FIND_LOCATION = "LOCATION";
    public static final String CONTENT_FIND_MY_BIKE = "FIND";
    public static final String CONTENT_UPDATE_PHONE = "SET_PHONE";

    public static String DEVICE_PHONE_NUMBER = "";
    public static String PASSWORD = "";

    private static Random r = new Random();

    public static final String encryptPassword(String password) {
        String result = "";
        for (int i = 0; i < password.length(); i++) {
            result += password.charAt(i) + randomString();
        }
        result += randomString();
        return result;
    }

    public static final String decryptPassword(String input, int numChar) {
        String password = "";
        while (password.length() < numChar) {
            password += input.charAt(0);
            input = input.substring(3);
        }
        return password;

    }

    private static final String randomString() {
        String alphabet = "0123456789qwertyuiopasdfghjkl:;'zxcvbnm,.<>/?[]{}`~!@#$%^&*()_-+=";
        return alphabet.charAt(r.nextInt(alphabet.length())) + "" + alphabet.charAt(r.nextInt(alphabet.length()));
    }
}
