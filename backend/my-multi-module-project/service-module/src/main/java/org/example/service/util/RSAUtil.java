package org.example.service.util;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA工具类
 * RSA是一种非对称加密算法，加密和解密使用不同的密钥（公钥和私钥）。
 * 特点：
 * - 安全性高：即使公钥被公开，也很难破解私钥。
 * - 速度慢：计算复杂，适合加密少量数据。
 */
public class RSAUtil {

    private static final String RSA_ALGORITHM = "RSA";

    // 硬编码的密钥对
    private static final String PUBLIC_KEY_BASE64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPIRFhtJorDVqC4LyrITnjWZABfKrZzauisnp3BNGzCWWFZ5hqtJSt39D/QSV5Wx/BWcnz+YnOzQ4xHWY6VE7Zmw9yVc4H8Y1G2lBJ4lAia7EZVlkgP3N1LHlgERIpgHnasnJoYpubM/g1Ne28EiZUHRyfO+qsuCr710vvdFL5HwIDAQAB";
    private static final String PRIVATE_KEY_BASE64 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM8hEWG0misNWoLgvKshOeNZkAF8qtnNq6KyencE0bMJZYVnmGq0lK3f0P9BJXlbH8FZyfP5ic7NDjEdZjpUTtmbD3JVzgfxjUbaUEniUCJrsRlWWSA/c3UseWAREimAedqycmhim5sz+DU17bwSJlQdHJ876qy4KvvXS+90UvkfAgMBAAECgYB8D9xvX1NwMgFinuWiwrX/nOeKXHehGXWSb8C2hSZtTFWIgDJkx2C+fG6cVRLzC0eL8hHqli6atWuos2lFyrWmwOWSTO43gud9Hlqu8P4ctecYW/518aEHUW6uvuysc3RWs4OiScdZBisDJ8/iwa5cF/QThFFS1VY00YExXz6D8QJBAPKa0CHZiWc6MHMAi96rI03fHQSvATcKoOdXe8NGyj8Q0QwlCSn0YYNdVpNAzV0aRa3Snidv0qXc+BXU9bizfXUCQQDakM/xP5uHNrfhTm3xnKNECcvHJyDRtngx12aOcMpuXYGmBrEcVrQ/WaG30yvsCxEKlLuEisUoqspUAXM3BqXDAkEA6nIrcprLywMTsP81K9DMxEM5wr3weaV+yzHiu1qctgdlTcw4p9+voIhB0vnLvA5YidtA1TGeKoV5BXgQohZqxQJBAIPz1XainjT9tfnSLdImbPMQrvQnDUtOzDXbA1GPiscisLkZZSfkuKFebaHbXbL942Xt8V/Nn8Yzhj89ON/JpZ0CQAi/gTPzQhDLYXm3gEa0EAfibG8LwKJMVA6m0MN11MJz0IAWKhjOR/jHIG/V0yDEpJwykLzNc8tk9GwbbJ7XI8o=";

    private static RSAPublicKey publicKey;
    private static RSAPrivateKey privateKey;

    // 在工具类加载时加载硬编码的密钥对
    static {
        try {
            publicKey = loadPublicKey(PUBLIC_KEY_BASE64);
            privateKey = loadPrivateKey(PRIVATE_KEY_BASE64);
        } catch (Exception e) {
            throw new RuntimeException("RSA密钥对加载失败", e);
        }
    }

    /**
     * 加载公钥
     *
     * @param publicKeyBase64 Base64编码的公钥字符串
     * @return RSAPublicKey 对象
     * @throws NoSuchAlgorithmException 如果算法不支持
     * @throws InvalidKeySpecException 如果密钥规范无效
     */
    private static RSAPublicKey loadPublicKey(String publicKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.decodeBase64(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return (RSAPublicKey) KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(keySpec);
    }

    /**
     * 加载私钥
     *
     * @param privateKeyBase64 Base64编码的私钥字符串
     * @return RSAPrivateKey 对象
     * @throws NoSuchAlgorithmException 如果算法不支持
     * @throws InvalidKeySpecException 如果密钥规范无效
     */
    private static RSAPrivateKey loadPrivateKey(String privateKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.decodeBase64(privateKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return (RSAPrivateKey) KeyFactory.getInstance(RSA_ALGORITHM).generatePrivate(keySpec);
    }

    public static String getPublicKeyStr(){
        return PUBLIC_KEY_BASE64;
    }

    /**
     * 获取公钥
     *
     * @return RSAPublicKey 对象
     */
    public static RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 获取私钥
     *
     * @return RSAPrivateKey 对象
     */
    public static RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 使用公钥加密字符串
     *
     * @param plainText 明文字符串
     * @return Base64编码的密文
     * @throws Exception 如果加密失败
     */
    public static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(encryptedBytes);
    }

    /**
     * 使用私钥解密字符串
     *
     * @param encryptedText Base64编码的密文
     * @return 解密后的明文
     * @throws Exception 如果解密失败
     */
    public static String decrypt(String encryptedText) throws Exception {
        byte[] decoded = Base64.decodeBase64(encryptedText);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(decoded);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 测试RSA工具类
     */
    public static void main(String[] args) {
        //生成RSA密钥对：公钥和私钥
        try {
            // 创建 KeyPairGenerator 对象
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024); // 使用 1024 位密钥长度

            // 生成密钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 将公钥和私钥转换为 Base64 编码的字符串
            String publicKeyBase64 = Base64.encodeBase64String(publicKey.getEncoded());
            String privateKeyBase64 = Base64.encodeBase64String(privateKey.getEncoded());

            // 输出 Base64 编码的公钥和私钥
            System.out.println("Public Key (Base64): " + publicKeyBase64);
            System.out.println("Private Key (Base64): " + privateKeyBase64);

            // 将这些字符串复制到你的代码中
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            // 加密
            String plainText = "Hello, RSA!";
            String encryptedText = encrypt(plainText);
            System.out.println("加密后的数据: " + encryptedText);

            // 解密
            String decryptedText = decrypt(encryptedText);
            System.out.println("解密后的数据: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}