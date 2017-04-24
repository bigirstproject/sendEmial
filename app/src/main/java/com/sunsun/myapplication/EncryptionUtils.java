package com.sunsun.myapplication;

import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by dexian on 4/27/16.
 */
public class EncryptionUtils {
    private EncryptionUtils() {
        //unused
    }

    public static String toMD5(String str) {
        if (str != null && !"".equals(str)) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
                byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < md5Byte.length; i++) {
                    sb.append(hex[(md5Byte[i] & 0xff) / 16]);
                    sb.append(hex[(md5Byte[i] & 0xff) % 16]);
                }
                str = sb.toString();
            } catch (Exception e) {
                Log.e("EncryptionUtils", e.getMessage());
            }
        }
        return str;
    }

}
