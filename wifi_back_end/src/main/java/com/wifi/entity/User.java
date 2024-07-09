package com.wifi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 定义用户实体
 * 使用JPA注解@Entity，并配置自增主键策略和唯一约束。
 */

@Entity
@Getter
@Setter
public class User {
    @Id
    //自增主键策略
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

}

