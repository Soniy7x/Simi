package io.simi.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * -------------------------------
 * 			StringUtils
 * -------------------------------
 *
 * createTime: 2015-04-12
 * updateTime: 2015-04-12
 *
 */
public final class StringUtils {

    /**
     * 私有化构造方法
     */
    private StringUtils(){}

    public static boolean isBankCardCode(String str){
        if (str == null || str.trim().length() == 0 || !str.matches("\\d+")) {
            return false;
        }
        char[] chs = str.trim().substring(0, str.length() - 1).toCharArray();
        int sum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            sum += k;
        }
        return str.charAt(str.length() - 1) == ((sum % 10 == 0) ? '0' : (char) ((10 - sum % 10) + '0'));
    }

    public static boolean isDateOfBirth(String date){
        if (date == null || date.trim().length() != 8 || !isNumber(date)) {
            return false;
        }
        date = date.trim();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6));
        if (year > 2200 || year < 1900 || month > 12 || month < 1 || day < 1) {
            return false;
        }
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            if (day > 31) {
                return false;
            }
        }else if (month == 2) {
            if (year % 4 == 0) {
                if (day > 29) {
                    return false;
                }
            }else {
                if (day > 28) {
                    return false;
                }
            }
        }else {
            if (day > 30) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean isEmail(String str){
        String pattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

    public static String encryptSHA256(String str){
        String ps = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            ps = transformHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            Log.e("Simi - StringUtils", e.getMessage());
            return "";
        }
        return ps;
    }

    public static String encryptMD5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            Log.e("Simi - StringUtils", e.getMessage());
            return "";
        } catch (UnsupportedEncodingException e) {
            Log.e("Simi - StringUtils", e.getMessage());
            return "";
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    public static String transformHex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
