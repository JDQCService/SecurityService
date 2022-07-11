package com.jianduanqingchang.securityservice.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author yujie
 */
public class PasswordUtil {

    private PasswordUtil(){}

    public static String getBCryptPassword(String password){
        return new BCryptPasswordEncoder().encode(password);
    }

    public static boolean matchBCryptPassword(String rawPassword, String encodedPassword){
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
