package org.example.service.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * AES: Advanced Encryption Standard 高级加密标准
 * 最常见的对称加密算法，即加密和解密使用同样的密钥，加密结果可逆
 * 特点：加密速度非常快，适合经常发送数据的场合
 */
public class AESUtil {

    private static final String KEY_ALGORITHM = "AES";

    private final Cipher decryptCipher;

    private final Cipher encryptCipher;

    private final SecretKey secretKey;

    /**
     * 构造函数，初始化AES工具类
     *
     * @param key 密钥字符串（必须是16、24或32字节长度）
     * @throws Exception 如果密钥长度不符合要求或初始化失败
     */
    public AESUtil(String key) throws Exception {
        this.secretKey = generateKeyFromSeed(key);
        decryptCipher = Cipher.getInstance(KEY_ALGORITHM);
        encryptCipher = Cipher.getInstance(KEY_ALGORITHM);
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
    }

    /**
     * 解密字符串
     *
     * @param content Base64编码的密文
     * @return 解密后的明文
     * @throws Exception 如果解密失败
     */
    public String decrypt(String content) throws Exception {
        byte[] bytes = Base64.decodeBase64(content);
        byte[] result = decryptCipher.doFinal(bytes);
        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * 加密字符串
     *
     * @param content 明文字符串
     * @return Base64编码的密文
     * @throws Exception 如果加密失败
     */
    public String encrypt(String content) throws Exception {
        byte[] result = encryptCipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(result);
    }

    /**
     * 从密钥字符串生成AES密钥
     *
     * @param seed 密钥字符串
     * @return 生成的AES密钥
     * @throws Exception 如果密钥长度不符合要求
     */
    private SecretKey generateKeyFromSeed(String seed) throws Exception {
        byte[] keyBytes = seed.getBytes(StandardCharsets.UTF_8);
        // 使用SHA-256哈希算法处理密钥字符串，确保长度符合AES要求
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        keyBytes = sha.digest(keyBytes);
        keyBytes = java.util.Arrays.copyOf(keyBytes, 16); // 截取前16字节作为AES密钥
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    /**
     * 将字节数组转换为十六进制字符串（用于调试）
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String toHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 测试AES工具类
     */
    public static void main(String[] args) {
        try {
            AESUtil aesUtil = new AESUtil("mySecretKey12345"); // 密钥字符串
            String originalText = "Hello, AES!";
            System.out.println("原始数据: " + originalText);

            String encryptedText = aesUtil.encrypt(originalText);
            System.out.println("加密后的数据: " + encryptedText);

            String decryptedText = aesUtil.decrypt(encryptedText);
            System.out.println("解密后的数据: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
