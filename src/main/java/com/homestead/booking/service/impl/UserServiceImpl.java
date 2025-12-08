package com.homestead.booking.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homestead.booking.common.ResultCode;
import com.homestead.booking.dto.UserLoginDTO;
import com.homestead.booking.dto.UserRegisterDTO;
import com.homestead.booking.dto.UserUpdateDTO;
import com.homestead.booking.entity.User;
import com.homestead.booking.exception.BusinessException;
import com.homestead.booking.mapper.UserMapper;
import com.homestead.booking.service.UserService;
import com.homestead.booking.utils.RedisUtil;
import com.homestead.booking.vo.LoginVO;
import com.homestead.booking.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 用户Service实现类
 *
 * @author homestead
 * @since 2025-11-18
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 用户注册
     */
    @Override
    public void register(UserRegisterDTO dto) {
        // 验证验证码
        String cacheKey = "verify_code:" + dto.getPhone();
        Object cacheCode = redisUtil.get(cacheKey);
        if (cacheCode == null) {
            throw new BusinessException(ResultCode.VERIFY_CODE_EXPIRED);
        }
        if (!dto.getVerifyCode().equals(cacheCode.toString())) {
            throw new BusinessException(ResultCode.VERIFY_CODE_ERROR);
        }

        // 检查用户名是否已存在
        LambdaQueryWrapper<User> usernameWrapper = new LambdaQueryWrapper<>();
        usernameWrapper.eq(User::getUsername, dto.getUsername());
        if (userMapper.selectCount(usernameWrapper) > 0) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }

        // 检查手机号是否已注册
        LambdaQueryWrapper<User> phoneWrapper = new LambdaQueryWrapper<>();
        phoneWrapper.eq(User::getPhone, dto.getPhone());
        if (userMapper.selectCount(phoneWrapper) > 0) {
            throw new BusinessException("该手机号已被注册");
        }

        // 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        user.setUserType(1); // 默认为普通用户
        user.setStatus(1); // 默认状态正常

        userMapper.insert(user);

        // 删除验证码缓存
        redisUtil.delete(cacheKey);

        log.info("用户注册成功: {}", dto.getUsername());
    }

    /**
     * 用户登录
     */
    @Override
    public LoginVO login(UserLoginDTO dto) {
        User user = null;

        // 密码登录
        if (dto.getLoginType() == 1) {
            if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
                throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "密码不能为空");
            }

            // 查询用户(支持用户名或手机号登录)
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(w -> w.eq(User::getUsername, dto.getUsername())
                    .or()
                    .eq(User::getPhone, dto.getUsername()));

            user = userMapper.selectOne(wrapper);

            if (user == null) {
                throw new BusinessException(ResultCode.USER_NOT_FOUND);
            }

            // 验证密码
            if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
                throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
            }
        }
        // 验证码登录
        else if (dto.getLoginType() == 2) {
            if (dto.getVerifyCode() == null || dto.getVerifyCode().isEmpty()) {
                throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "验证码不能为空");
            }

            // 验证验证码
            String cacheKey = "verify_code:" + dto.getUsername();
            Object cacheCode = redisUtil.get(cacheKey);
            if (cacheCode == null) {
                throw new BusinessException(ResultCode.VERIFY_CODE_EXPIRED);
            }
            if (!dto.getVerifyCode().equals(cacheCode.toString())) {
                throw new BusinessException(ResultCode.VERIFY_CODE_ERROR);
            }

            // 查询用户
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, dto.getUsername());
            user = userMapper.selectOne(wrapper);

            if (user == null) {
                throw new BusinessException(ResultCode.USER_NOT_FOUND);
            }

            // 删除验证码缓存
            redisUtil.delete(cacheKey);
        } else {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "不支持的登录方式");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        StpUtil.login(user.getId());
        // 生成Token
        String token = StpUtil.getTokenValue();

        // 转换为VO
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);

        log.info("用户登录成功: {}", user.getUsername());

        return new LoginVO(token, userVO);
    }

    /**
     * 发送验证码
     */
    @Override
    public void sendVerifyCode(String phone) {
        // 检查发送频率(60秒内只能发送一次)
        String cacheKey = "verify_code:" + phone;
        if (redisUtil.hasKey(cacheKey)) {
            throw new BusinessException(ResultCode.VERIFY_CODE_SEND_FREQUENTLY);
        }

        // 生成6位验证码
        String verifyCode = RandomUtil.randomNumbers(6);

        // 存入Redis,5分钟过期
        redisUtil.set(cacheKey, verifyCode, 5, TimeUnit.MINUTES);

        // TODO: 调用阿里云短信服务发送验证码
        // 这里暂时只记录日志,实际项目中需要调用短信接口
        log.info("发送验证码到手机号 {}: {}", phone, verifyCode);

        // 开发环境下,可以直接在控制台输出验证码方便测试
        System.out.println("===========================");
        System.out.println("验证码: " + verifyCode);
        System.out.println("===========================");
    }

    /**
     * 获取用户信息
     */
    @Override
    public UserVO getUserInfo() {
        long userId =StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        return BeanUtil.copyProperties(user, UserVO.class);
    }

    /**
     * 更新用户信息
     */
    @Override
    public void updateUserInfo(Long userId, UserUpdateDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 更新信息
        if (dto.getNickname() != null) {
            user.setNickname(dto.getNickname());
        }
        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (dto.getBirthday() != null) {
            user.setBirthday(dto.getBirthday());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        userMapper.updateById(user);

        log.info("用户信息更新成功: {}", userId);
    }

    /**
     * 修改密码
     */
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 验证旧密码
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 更新密码
        user.setPassword(BCrypt.hashpw(newPassword));
        userMapper.updateById(user);

        log.info("用户修改密码成功: {}", userId);
    }

}
