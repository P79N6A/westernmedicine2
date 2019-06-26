package com.xywy.askforexpert.appcommon.utils.enctools;


import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA 加密
 * <p>
 * created by shihao
 */
public class RSATools {

    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4QVQ+n54HCCFMHuGikTD0GMDxHgB8utMoszl955dcl6ax5YOMTa1z4Ib815/PGbRCBDv0vsG9jeGKY1pe9Qj3KHxJKiicJr3KV1R1vmzv1JdcRNTFVb6I9/awbJTNnTOvl8JZNm8QomdHlrQk8u3vP/Xdj217Mk4I4mTGDK1WFwIDAQAB";

    public static String strRSA(String pwd) {
        String contacts = "";
        byte[] keyByte;
        try {
            keyByte = Base64.decode(PUBLIC_KEY);
            X509EncodedKeySpec x509ek = new X509EncodedKeySpec(keyByte);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509ek);

            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] sbt = pwd.getBytes();
            byte[] epByte = cipher.doFinal(sbt);
            contacts = Base64.encode(epByte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contacts;
    }

}
