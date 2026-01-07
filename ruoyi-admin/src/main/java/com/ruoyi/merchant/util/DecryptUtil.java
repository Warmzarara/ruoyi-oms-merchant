package com.ruoyi.merchant.util;

import cn.hutool.core.util.StrUtil;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DecryptUtil {
    public static void main(String[] args) {
        String ACCESS_SECRET = "5c3c2e7f-32de-4e98-9ccd-7e5a0139f943";
        System.out.println("ACCESS_SECRET: " + ACCESS_SECRET);
        
        String APP_KEY = "7210436283015906876";
        System.out.println("APP_KEY: " + APP_KEY);

        String PARAM_JSON = "{\n" +
                "    \"orderId\": \"1298521621799\",\n" +
                "    \"cancelReason\": \"菜鸟裹裹测试取消\"\n" +
                "}";
        System.out.println("PARAM_JSON: " + PARAM_JSON);
        
        // 时间戳 2025 年 12 月 08 日 10:47:50
        String TIMESTAMP = "1765162070";
        System.out.println("TIMESTAMP: " + TIMESTAMP);
        
        String USER_TYPE = "EMS";
        System.out.println("USER_TYPE: " + USER_TYPE);
        
        String PARAM = "app_key" + APP_KEY + "param_json" + PARAM_JSON + "timestamp" + TIMESTAMP;
        
        String SIGN_PATTERN = ACCESS_SECRET + PARAM + ACCESS_SECRET;
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("MD5").digest(SIGN_PATTERN.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("验签错误");
        }
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        while (md5code.length() < 32) {
            md5code.insert(0, "0");
        }
        System.out.println(md5code);
    }
}
