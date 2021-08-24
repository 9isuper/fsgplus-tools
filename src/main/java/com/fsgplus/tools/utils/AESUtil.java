package com.fsgplus.tools.utils;


import com.isuper.eden.adam.encry.AESType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@Slf4j
public class AESUtil {

    private static final String KEY_ALGORITHM = "AES";

    private static final String CONTENT_ENCODE = "utf-8";
    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";


    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key     加密密钥
     * @param type    TYPE
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String key, AESType type) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            byte[] byteContent = content.getBytes(CONTENT_ENCODE);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key, type));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //通过Base64转码返回
            return Base64.encodeBase64URLSafeString(result);
        } catch (Exception ex) {
            log.error("Exception", ex);
        }

        return null;
    }


    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key     加密密钥
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String key) {
        return encrypt(content, key, AESType.AES_128);
    }

    /**
     * @param content
     * @param key
     * @param type
     * @return
     */
    public static String encryptByHex(String content, String key, AESType type) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            byte[] byteContent = content.getBytes(CONTENT_ENCODE);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key, type));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //通过Base64转码返回
            return AESUtils.parseByte2HexStr(result);
        } catch (Exception ex) {
            log.error("Exception", ex);
        }

        return null;
    }


    /**
     * @param content
     * @param key
     * @return
     */
    public static String encryptByHex(String content, String key) {
        return encryptByHex(content, key, AESType.AES_128);
    }


    /**
     * AES 解密操作
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, String key) {
        return decrypt(content, key, AESType.AES_128);
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, String key, AESType aesType) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key, aesType));
            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));

            return new String(result, CONTENT_ENCODE);
        } catch (Exception ex) {
            log.error("Exception", ex);
        }

        return null;
    }


    /**
     * AES 解密操作
     *
     * @param content
     * @param key
     * @return
     */
    public static String decryptForHex(String content, String key) {
        return decryptForHex(content, key, AESType.AES_128);
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param key
     * @return
     */
    public static String decryptForHex(String content, String key, AESType aesType) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key, aesType));
            //执行操作
            byte[] result = cipher.doFinal(AESUtils.parseHexStr2Byte(content));

            return new String(result, CONTENT_ENCODE);
        } catch (Exception ex) {
            log.error("Exception", ex);
        }

        return null;
    }


    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(String key, AESType type) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象,
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);

            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(key.getBytes(CONTENT_ENCODE));
            kg.init(type.getType(), random);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (Exception ex) {
            log.error("Exception", ex);
        }

        return null;
    }

    public static void main(String[] args) {
        String content = "VJM6GacuXiPJLbE5x95aqvgD_eMRktCxixzzNhW1GDt0YvFfT4H5FPaHjHbkUd7UmduR5smv2L8ccKbtGRYmZm1LA0x-59u9-iXg7hwAhmU";
        String key = "efesco@*";
        System.out.println("content:" + content);
        String value =  AESUtil.decrypt(content, key);
        System.out.println("decryptContent:" +value);
        System.out.println("encryptByHex:" +AESUtil.encryptByHex(value,key));
        System.out.println("decryptForHex:" +AESUtil.decryptForHex(AESUtil.encryptByHex(value,key),key));

    }
}
