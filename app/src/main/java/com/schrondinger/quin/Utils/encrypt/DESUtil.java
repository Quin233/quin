package com.schrondinger.quin.Utils.encrypt;

import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import it.sauronsoftware.base64.Base64;

public class DESUtil {
    /**
     * DES加密
     */
    public static String desEncode(String key,String siv,String data) throws Exception{
        try{
            DESKeySpec dks = new DESKeySpec(key.getBytes());

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(siv.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);
            byte[] bytes = cipher.doFinal(data.getBytes());

            return new String(Base64.encode(bytes));
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     * DES解密
     */
    public static byte[] desDecode(String key,String siv,byte[] data) throws Exception{
        try{
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(siv.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey,paramSpec);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     * 开始解密（String）
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    public static String decodeValue(String key,String siv,String data) {
        byte[] datas;
        String value = null;
        try {
            datas = desDecode(key, siv,Base64.decode(data.getBytes()));
            value = new String(datas);
        } catch (Exception e) {
            value = "";
        }
        return value;
    }
}
