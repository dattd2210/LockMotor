package com.lockmotor.global;

import java.util.Random;

/**
 * Created by trandinhdat on 7/26/16.
 */
public class GlobalConstant {
    public static final String DEVICE_PHONE_NUMBER_KEY = "DEVICE_PHONE_NUMBER";
    public static final String PASSWORD_KEY = "PASSWORD_KEY";
    public static final String PASSWORD_LENGTH_KEY = "PASSWORD_LENGTH";
    public static final String PHONE_NUMBER_KEY = "PHONE_NUMBER_KEY";
    public static final String ANTI_THIEF_STATUS_KEY = "ANTI_THIEF_STATUS_KEY";
    public static final String FINGER_ID_KEY = "FINGER_ID_KEY";
    public static final String SHARE_PREFERENCES_NAME = "SHARE_PREFERENCES_NAME";
    public static final String CONTENT_UNLOCK = "UNLOCK";
    public static final String CONTENT_LOCK = "LOCK";
    public static final String CONTENT_FIND_LOCATION = "LOCATION";
    public static final String CONTENT_END_FIND_LOCATION = "OFF_LOCATION";
    public static final String CONTENT_FIND_MY_BIKE = "FIND";
    public static final String CONTENT_UPDATE_PHONE = "SET_PHONE";
    public static final String CONTENT_UPDATE_FINGER = "SET_FINGER";
    public static final String CONTENT_DELETE_FINGER = "DELETE_FINGER";
    public static final String UNKNOW_NET_PROVIDER = "Chưa xác định";
    public static final int AUTO_CALL_API_TIME = 5000;
    public static final int MAX_WAITING_TIME = 60000;

    public static final int CHECK_ACCOUNT_ID = 0;
    public static final int RECHARGE_ACCOUNT_ID = 1;
    public static final int FINGER_UPDATE_ID = 2;
    public static final int PHONE_NUMBER_UPDATE_ID = 3;
    public static final int FINGER_DELETE_ID = 4;

    //Request permission code
    public static final int REQUEST_PERMISSION_CODE = 1;

    //Recharge content
    public static final String RECHARGE = "*100*";

    //Account check content
    public static final String CHECK_ACCOUNT_STRING = "*101#";

    public static String PHONE_NUMBER = "";
    public static String DEVICE_PHONE_NUMBER = "";
    public static String PASSWORD = "";
    public static int PASSWORD_LENGTH = 0;
    public static boolean HAS_OPEN_SETTING = false;
    public static String FINGER_ID_LIST = "";
    public static String ADDED_ID = "";
    public static String DELETE_ID = "";

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

    public static final String decryptPhoneNumber(){
        String decryptCode = "AFHG79BCIK";
        String decryptedPhoneNumber = "";
        for(int i = 0; i < PHONE_NUMBER.length(); i++){
            decryptedPhoneNumber += decryptCode.charAt(Integer.parseInt(PHONE_NUMBER.charAt(i)+""));
        }
        return decryptedPhoneNumber;
    }

    public static String getNextFingerID() {
        for (int i = 1; i < 5; i++) {
            if (!FINGER_ID_LIST.contains(i + "")) {
                return i + "";
            }
        }
        return "";
    }

//    public static final String getRechargeString(){
//        switch (PhoneUtils.getNetProviderFromPhoneNumber(DEVICE_PHONE_NUMBER)){
//            case 1: //VIETEL
//                return RECHARGE_VIETEL;
//            case 2: //MOBIFONE
//                return
//            default:
//                return "";
//        }
//    }
}
