package com.wifi.service;

import com.wifi.entity.User;
import com.wifi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 处理用户注册和登录的业务逻辑
 * 通过UserRepository与数据库交互，实现用户注册和登录功能
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String username, String password) {
        // 检查用户是否已经存在
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("User already exists");
        }

        // 创建新用户并保存到数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 生产环境中需要加密密码
        userRepository.save(user);
        return user;
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            //如果用户不存在或者账密不匹配
            throw new RuntimeException("Invalid username or password");
        }
        return user;
    }
}
