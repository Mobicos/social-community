package org.example.service.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * MD5工具类
 * MD5是一种哈希算法，用于生成数据的“指纹”。
 * 特点：
 * - 速度快：计算MD5很高效。
 * - 不安全：虽然MD5的指纹很难被篡改，但现在已经有一些方法可以伪造MD5，所以它不适合用于重要的安全场景。
 */
public class MD5Util {

	/**
	 * 生成字符串的MD5签名
	 *
	 * @param content 原始内容
	 * @param salt    盐值（用于增强安全性）
	 * @param charset 字符编码（如UTF-8）
	 * @return MD5哈希值
	 */
	public static String sign(String content, String salt, String charset) {
		content = content + salt;
		return DigestUtils.md5Hex(getContentBytes(content, charset));
	}

	/**
	 * 验证字符串的MD5签名
	 *
	 * @param content 原始内容
	 * @param sign    已签名的MD5值
	 * @param salt    盐值
	 * @param charset 字符编码
	 * @return 是否验证通过
	 */
	public static boolean verify(String content, String sign, String salt, String charset) {
		content = content + salt;
		String mySign = DigestUtils.md5Hex(getContentBytes(content, charset));
		return mySign.equals(sign);
	}

	/**
	 * 将字符串转换为字节数组
	 *
	 * @param content 原始内容
	 * @param charset 字符编码
	 * @return 字节数组
	 */
	private static byte[] getContentBytes(String content, String charset) {
		if (charset != null && !charset.isEmpty()) {
			try {
				return content.getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("指定的编码集错误: " + charset, e);
			}
		} else {
			return content.getBytes(StandardCharsets.UTF_8); // 默认使用UTF-8
		}
	}

	/**
	 * 获取文件的MD5值
	 *
	 * @param file 文件对象
	 * @return 文件的MD5哈希值
	 * @throws IOException 如果文件读取失败
	 */
	public static String getFileMD5(MultipartFile file) throws IOException {
		try (InputStream fis = file.getInputStream()) {
			return DigestUtils.md5Hex(fis); // 直接对文件流计算MD5，避免加载整个文件到内存
		}
	}

	/**
	 * 测试MD5工具类
	 */
	public static void main(String[] args) {
		try {
			// 测试字符串签名
			String content = "Hello, MD5!";
			String salt = "mySecretSalt";
			String charset = "UTF-8";
			String sign = MD5Util.sign(content, salt, charset);
			System.out.println("签名: " + sign);

			// 验证签名
			boolean verified = MD5Util.verify(content, sign, salt, charset);
			System.out.println("验证结果: " + verified);

			// 测试文件MD5
			// MultipartFile file = ...; // 假设从文件上传中获取
			// String fileMD5 = MD5Util.getFileMD5(file);
			// System.out.println("文件MD5: " + fileMD5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}