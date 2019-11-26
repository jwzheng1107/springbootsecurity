package com.jw.learn.security.config;

import com.jw.learn.security.entity.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义认证过程
 * 实现Spring Security提供的UserDetailService接口
 */
@Configuration
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //会接收登录表单username
        // 模拟一个用户，替代数据库获取逻辑
        MyUser user = new MyUser();
        user.setUserName(username);
        user.setPassword(this.passwordEncoder.encode("123456"));
        // 输出加密后的密码
        System.out.println(user.getPassword());

        /**
         * Spring Security提供的UserDetails接口实现类User:
         *   getAuthorities获取用户包含的权限，返回权限集合，权限是一个继承了GrantedAuthority的对象；
         *   getPassword和getUsername用于获取密码和用户名；
         *   isAccountNonExpired方法返回boolean类型，用于判断账户是否未过期，未过期返回true反之返回false；
         *   isAccountNonLocked方法用于判断账户是否未锁定；
         *   isCredentialsNonExpired用于判断用户凭证是否没过期，即密码是否未过期；
         *   isEnabled方法用于判断用户是否可用。
         *
         * 由于权限参数不能为空，所以这里先使用AuthorityUtils.commaSeparatedStringToAuthorityList方法模拟一个admin的权限，
         * 该方法可以将逗号分隔的字符串转换为权限集合
         */
        return new User(username, user.getPassword(), user.isEnabled(),  //表单密码跟User的密码对应不上则不通过验证
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
