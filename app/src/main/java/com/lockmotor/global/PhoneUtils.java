package com.lockmotor.global;

/**
 * Created by trandinhdat on 9/9/16.
 */
public class PhoneUtils {
    public static int getNetProviderFromPhoneNumber(String phoneNumber) {
        String phonePre = "";
        if (phoneNumber.startsWith("09") || phoneNumber.startsWith("08")) {
            phonePre = phoneNumber.substring(0, 3) + "x";
        }
        if (phoneNumber.startsWith("01")) {
            phonePre = phoneNumber.substring(0, 4);
        }

        switch (phonePre) {
            case "096x":
            case "097x":
            case "098x":
            case "0162":
            case "0163":
            case "0164":
            case "0165":
            case "0166":
            case "0167":
            case "0168":
            case "0169":
                return 1;

            case "091x":
            case "094x":
            case "088x":
            case "0123":
            case "0124":
            case "0125":
            case "0127":
            case "0129":
                return 3;

            case "095x":
                return 6;

            case "090x":
            case "093x":
            case "089x":
            case "0120":
            case "0121":
            case "0122":
            case "0126":
            case "0128":
                return 2;

            case "092x":
            case "0188":
            case "0186":
                return 4;

            case "099x":
            case "0199":
                return 5;
        }
        return 0;
    }
}
