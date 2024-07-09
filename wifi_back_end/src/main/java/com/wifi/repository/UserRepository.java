package com.wifi.repository;

import com.wifi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 定义数据访问接口
 * 使用JpaRepository实现数据访问，并定义findByUsername方法，用于根据用户名查询用户。
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}

