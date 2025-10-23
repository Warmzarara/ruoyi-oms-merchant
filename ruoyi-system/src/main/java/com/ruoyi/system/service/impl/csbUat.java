//package com.ruoyi.system.service.impl;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONObject;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.net.util.Base64;
//
//import com.alibaba.csb.sdk.HttpCaller;
//
//
//public class csbUat {
//
//    public static void main(String[] args) throws ParseException {
//
//        Map<String, String> params = new HashMap<String, String>();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String param1 = "[\"1289910596497\"]";
//
//        JSONObject msg = new JSONObject();
//        msg.put("sysCode", "test_uat");
//        msg.put("serialNo", UUID.randomUUID().toString());
//        msg.put("sendDate", sdf.format(new Date()));
//        msg.put("sign", getSign("CSHIPING12", param1));
//
//
//        String msgs = msg.toJSONString(); // 转换为JSON String
//        System.out.println(msgs);
//        params.put("messageHeader", msgs);
//        params.put("waybillNos", param1);
//
//
//        // csb处理
//        String requestURL = "http://211.156.216.240:8086/ceshi_csb_broker";
//        String API_NAME = "orderInternalQueryService"; // CSB发布服务定义的服务名
////        String API_NAME = "queryRealNameInfoByCSB"; // CSB发布服务定义的服务名
//        String version = "1.0.0";
//        String ak = "dd979a77b2a44e54b65f01dcbc0bae04";
//        String sk = "kCZfusS4Zn300MTnYtIlWzhdoE8="; // 用户安全校验的签名密钥对
//
//        try {
//            String result = HttpCaller.doPost(requestURL, API_NAME, version, params, ak, sk);
//            if (result != null) {
//                // 返回结果处理, 如转换为JSON对象
//                JSONObject jsonObject = JSON.parseObject(result);
//
//                System.out.println(jsonObject.toJSONString());
//
////                getFile(jsonObject.getJSONObject("body").getString("retBody").getBytes(CharEncoding.ISO_8859_1), "D:/oms/", "1.pdf");
//            }
//            else {
//                System.out.println("没有任何数据。");
//            }
//        }
//        catch (Exception e) {
//            // 调用异常处理
//            System.out.println(e);
//        }
//
//    }
//
//    public static void getFile(byte[] bfile, String filePath, String fileName) {
//        BufferedOutputStream bos = null;
//        FileOutputStream fos = null;
//        File file = null;
//        try {
//            File dir = new File(filePath);
//            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
//                dir.mkdirs();
//            }
//            file = new File(filePath + "\\" + fileName);
//            fos = new FileOutputStream(file);
//            bos = new BufferedOutputStream(fos);
//            bos.write(bfile);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            if (bos != null) {
//                try {
//                    bos.close();
//                }
//                catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//            if (fos != null) {
//                try {
//                    fos.close();
//                }
//                catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static String getSign(String password, String... params) {
//        StringBuffer sb = new StringBuffer(password);
//        if (params != null) {
//            for (String o : params) {
//                sb.append(o);
//            }
//        }
//        System.out.println(sb.toString());
//        String mysign = Base64
//            .encodeBase64String(DigestUtils.md5Hex(DigestUtils.md5Hex(sb.toString())).getBytes())
//            .replaceAll("\r|\n", "");
//        return mysign;
//    }
//}
