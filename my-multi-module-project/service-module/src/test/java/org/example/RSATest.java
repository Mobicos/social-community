package org.example;

import junit.framework.TestCase;
import org.example.service.util.RSAUtil;

/**
 * Unit test for simple App.
 */
public class RSATest extends TestCase {
    public void testDecrypt() {
        try {
            // 加密
            String plainText = "123456";
            String encryptedText = RSAUtil.encrypt(plainText);
            System.out.println("加密后的数据: " + encryptedText);

            // 解密
            String decryptedText = RSAUtil.decrypt(encryptedText);
            System.out.println("解密后的数据: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
