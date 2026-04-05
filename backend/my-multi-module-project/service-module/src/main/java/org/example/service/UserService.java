package org.example.service;

import org.apache.commons.lang3.StringUtils;
import org.example.dao.UserDao;
import org.example.dao.UserInfoDao;
import org.example.model.RefreshToken;
import org.example.model.User;
import org.example.model.UserInfo;
import org.example.model.constant.UserConstant;
import org.example.model.exception.ConditionException;
import org.example.service.util.MD5Util;
import org.example.service.util.RSAUtil;
import org.example.service.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Transactional
    public void register(User user) {
        // 检查手机号是否为空
        String phone = user.getPhone();
        if (phone == null || phone.trim().isEmpty()) {
            throw new ConditionException("手机号不能为空！");
        }

        // 检查手机号是否已注册
        User dbUser = userDao.getUserByPhone(phone);
        if (dbUser != null) {
            throw new ConditionException("该手机号已经注册！");
        }

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 用私钥解密密码
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        // 生成盐值
        String salt = String.valueOf(now.toEpochSecond(ZoneOffset.UTC));

        // 加密密码
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");

        // 设置用户信息
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        userDao.insertUser(user);

        // 添加用户附加信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(now);
        userInfo.setGender(Integer.valueOf(UserConstant.GENDER_MALE));
        userInfo.setCreateTime(now);
        userInfoDao.insertUserInfo(userInfo);
    }

    public String login(User user) throws Exception {
        // 获取手机号和邮箱
        String phone = Optional.ofNullable(user.getPhone()).orElse("");
        String email = Optional.ofNullable(user.getEmail()).orElse("");

        // 检查参数是否为空
        if (StringUtils.isBlank(phone) && StringUtils.isBlank(email)) {
            throw new ConditionException("手机号或邮箱不能为空！");
        }

        // 查询用户
        User dbUser = userDao.getUserByPhoneOrEmail(phone, email);
        if (dbUser == null) {
            throw new ConditionException("当前用户不存在！");
        }

        // 解密密码
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        // 比对密码
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("密码错误！");
        }

        // 生成并返回令牌
        return TokenUtil.generateToken(dbUser.getId());
    }

    /**
     * 获取当前用户信息：
     *
     * @param userId
     * @return
     */
    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userInfoDao.getUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    public User getUserById(Long followingId) {
        return userDao.getUserById(followingId);
    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> userIds) {
        return userInfoDao.getUserInfoByUserIds(userIds);
    }

    /**
     * 双token登录：同时获取访问令牌和刷新令牌
     *
     * @param user
     * @return
     * @throws Exception
     */
    public Map<String, Object> loginDts(User user) throws Exception {
        // 使用Optional和StringUtils来简化空值处理
        String phone = Optional.ofNullable(user.getPhone()).orElse("");
        String email = Optional.ofNullable(user.getEmail()).orElse("");

        // 检查手机号和邮箱是否都为空
        if (StringUtils.isBlank(phone) && StringUtils.isBlank(email)) {
            throw new ConditionException("参数异常！");
        }

        // 根据手机号或邮箱查询用户信息
        User dbUser = userDao.getUserByPhoneOrEmail(phone, email);
        if (dbUser == null) {
            throw new ConditionException("当前用户不存在！");
        }

        // 获取用户输入的密码并解密
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(user.getPassword());
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        // 验证密码
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("密码错误！");
        }

        // 获取用户ID并生成token
        Long userId = dbUser.getId();
        String accessToken = TokenUtil.generateToken(userId);
        String refreshToken = TokenUtil.generateRefreshToken(userId);

        // 更新refreshToken
        userDao.deleteRefreshTokenByUserId(userId);
        userDao.addRefreshToken(refreshToken, userId);

        // 使用HashMap构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);

        return result;
    }

    public void logout(Long userId) {
        userDao.deleteRefreshTokenByUserId(userId);
    }

    /**
     * 刷新访问令牌: 如果用户未退出登录，当`access-token`过期时，使用`refresh-token`刷新获取新的`access-token`。
     *
     * @param refreshToken
     * @return
     * @throws Exception
     */
    public String refreshAccessToken(String refreshToken) throws Exception {
        Long userId = TokenUtil.verifyToken(refreshToken);
        if(userId < 0) {
            throw new ConditionException("非法用户");
        }
        RefreshToken refreshTokenDetail = userDao.getRefreshToken(refreshToken);
        if(refreshTokenDetail == null){
            throw new ConditionException("555","refreshToken已过期或无效！");
        }
        // 获取用户ID并生成token，因为accessToken已过期，无法调用UserTokenService来获取userId了
        userId = refreshTokenDetail.getUserId();
        // 重新生成accessToken
        return TokenUtil.generateToken(userId);
    }

    public String getRefreshTokenByUserId(Long userId) {
        return userDao.getRefreshTokenByUserId(userId);
    }

}