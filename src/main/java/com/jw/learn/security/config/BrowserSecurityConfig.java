package com.jw.learn.security.config;

import com.jw.learn.security.component.MyAuthenticationFailureHandler;
import com.jw.learn.security.component.MyAuthenticationSucessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired private MyAuthenticationSucessHandler authenticationSucessHandler;

  @Autowired private MyAuthenticationFailureHandler authenticationFailureHandler;

  /**
   * PasswordEncoder是一个密码加密接口，而BCryptPasswordEncoder是Spring Security提供的一个实现方法，
   * 我们也可以自己实现PasswordEncoder。不过Spring Security实现的BCryptPasswordEncoder已经足够强大，
   * 它对相同的密码进行加密后可以生成不同的结果。
   *
   * @return
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.formLogin() // 表单方式
        // http.httpBasic() // HTTP Basic
        .loginPage("/login.html") // 登录跳转 URL
        //                .loginPage("/authentication/require") // 登录跳转 URL
        .loginProcessingUrl("/login") // 处理表单登录 URL      loginProcessingUrl----->（重定向）loginPage
        .successHandler(authenticationSucessHandler) // 处理登录成功
        .failureHandler(authenticationFailureHandler) // 处理登录失败
        .and()
        .authorizeRequests() // 授权配置
        .antMatchers("/authentication/require", "/login.html")
        .permitAll() // 登录跳转 URL 无需认证
        .anyRequest() // 所有请求
        .authenticated() // 都需要认证
        .and()
        .csrf()
        .disable(); // CSRF攻击防御关了
  }
}
