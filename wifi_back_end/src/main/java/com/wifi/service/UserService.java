package com.wifi.service;

import com.wifi.entity.User;
import com.wifi.entity.VerificationToken;
import com.wifi.exception.EmailAlreadyExistsException;
import com.wifi.exception.InvalidTokenException;
import com.wifi.exception.UserAlreadyExistsException;
import com.wifi.repository.UserRepository;
import com.wifi.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 处理用户注册和登录的业务逻辑
 * 通过UserRepository与数据库交互，实现用户注册和登录功能
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public User registerUser(String username, String password, String email, String token) {

        //检查验证码有效性
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Invalid or expired verification token");
        }

        // 检查用户是否已经存在
        if (userRepository.findByUsername(username) != null) {
            throw new UserAlreadyExistsException("User already exists");
        }

        // 检查邮箱是否已经存在
        if(userRepository.findByEmail(email) != null) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // 创建新用户并保存到数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 生产环境中需要加密密码
        user.setEmail(email);

        userRepository.save(user);
        tokenRepository.delete(verificationToken);

        return user;
    }

    //发送验证码
    public String sendVerificationToken(String email) {

        //TODO: UUID在实际用的时候好使、开发的时候简单点
//        String token = UUID.randomUUID().toString();
        //此处生成随机六位数数字验证码
        String token = String.format("%06d", (int)(Math.random()*900000)+100000);

        //60s内有效
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(60);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setEmail(email);
        verificationToken.setExpiryDate(expiryDate);

        tokenRepository.save(verificationToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1290753692@qq.com");
        message.setTo(email);
        message.setSubject("Email Verification");
        message.setText("Your verification code is: " + token);

        try {
            emailSender.send(message);
        } catch (MailException e) {
            tokenRepository.delete(verificationToken);
            throw new RuntimeException("Failed to send verification email: " + e.getMessage());
        }

        return token;
    }

    public User loginUser(String identifier, String password) {
        User user = userRepository.findByUsername(identifier);
        if (user == null) {
            user = userRepository.findByEmail(identifier);
        }
        if (user == null || !user.getPassword().equals(password)) {
            // 如果用户不存在或者账密不匹配
            throw new RuntimeException("Invalid username/email or password");
        }
        return user;
    }
}
