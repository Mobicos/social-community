package org.example.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.model.exception.ConditionException;

import java.util.Calendar;
import java.util.Date;

/**
 * Token 工具类，用于生成和验证 JWT Token。
 */
public class TokenUtil {

    /**
     * JWT Token 的签发者标识。
     */
    private static final String ISSUER = "签发者";

    /**
     * 生成一个有效期为 1 小时的 JWT Token。
     *
     * @param userId 用户 ID，作为 Token 的唯一标识。
     * @return 生成的 JWT Token 字符串。
     * @throws Exception 如果生成 Token 过程中发生错误，抛出异常。
     */
    public static String generateToken(Long userId) throws Exception {
        // 使用 RSA256 算法，通过 RSAUtil 获取公钥和私钥
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());

        // 创建一个日历实例，设置 Token 的过期时间为当前时间 + 1 小时
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1);

        // 创建 JWT Token
        return JWT.create()
                .withKeyId(String.valueOf(userId)) // 将用户 ID 作为 Key ID
                .withIssuer(ISSUER) // 设置签发者
                .withExpiresAt(calendar.getTime()) // 设置过期时间
                .sign(algorithm); // 使用算法签名
    }

    /**
     * 验证 JWT Token 的有效性，并提取用户 ID。
     *
     * @param token 需要验证的 JWT Token。
     * @return 验证通过后提取的用户 ID。
     * @throws ConditionException 如果 Token 验证失败，抛出自定义异常。
     */
    public static Long verifyToken(String token) {
        try {
            // 使用 RSA256 算法，通过 RSAUtil 获取公钥和私钥
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());

            // 创建 JWT 验证器
            JWTVerifier verifier = JWT.require(algorithm).build();

            // 验证 Token
            DecodedJWT jwt = verifier.verify(token);

            // 提取用户 ID（存储在 Key ID 中）
            String userId = jwt.getKeyId();
            return Long.valueOf(userId);
        } catch (TokenExpiredException e) {
            // 如果 Token 过期，抛出自定义异常
            throw new ConditionException("555", "Token过期！");
        } catch (Exception e) {
            // 如果 Token 验证失败，抛出自定义异常
            throw new ConditionException("非法用户Token！");
        }
    }

    public static String generateRefreshToken(Long userId) throws Exception{
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
    }

    public static void main(String[] args) {
        // 测试生成和验证Token：需要分别执行，不能一起执行哦
//        try {
//            String token = TokenUtil.generateToken(12345L); // 假设用户 ID 为 12345
//            System.out.println("Generated Token: " + token);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            Long userId = TokenUtil.verifyToken("eyJraWQiOiIxMjM0NSIsInR5cCI6IkpXVCIsImFsZyI6IlJTMjU2In0.eyJpc3MiOiLnrb7lj5HogIUiLCJleHAiOjE3NDE0MjQyODV9.tHEP1-55QxzVTkhziLyBfc1e7RPL6IUuyQ_zYhAF9G6kpRYtDethSh6A3MOVDqnXDqgpn8ep5R4V-rbLLC3tcWJqSPAZk6WXiwBLzANXOL_6lzIQjrVXc0e0m7tp-M7kY_lOfgrPKbYjg5oJ8Y7JHdNCnJf9VLJ-OyEqDmpnfdw");
            System.out.println("Verified User ID: " + userId);
        } catch (ConditionException e) {
            System.out.println(e.getMessage());
        }
    }
}