package com.example.onlinemusic.tools;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    //定义一个固定的盐值
    private static final String salt = "1b2i3t4e";


    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(1)+salt.charAt(3) + inputPass
                +salt.charAt(5) + salt.charAt(6);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5)
                + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }


    public static void main(String[] args) {
        System.out.println("对用户输入密码进行第1次加密："+inputPassToFormPass("123456"));

        System.out.println("对用户输入密码进行第2次加密："+formPassToDBPass(inputPassToFormPass("123456"),
                "1b2i3t4e"));

        System.out.println("对用户输入密码进行第2次加密："+inputPassToDbPass("123456", "1b2i3t4e"));
    }
}
