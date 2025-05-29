package com.example.SBEAM.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF，因为我们使用Token认证
                .csrf().disable()
                // 允许所有请求访问（我们使用自定义的拦截器进行认证）
                .authorizeRequests()
                .anyRequest().permitAll()
                // 禁用默认的登录页面和HTTP Basic认证
                .and()
                .formLogin().disable()
                .httpBasic().disable();
    }
}