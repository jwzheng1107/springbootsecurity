1.替代数据库获取用户逻辑    
实现UserDetailService  
***
2.密码加密  
使用PasswordEncoder，常用的实现：BCryptPasswordEncoder
***
3.替换默认登录页  
loginPage loginProcessingUrl
***
4.缓存请求和处理重定向  
HttpSessionRequestCache DefaultRedirectStrategy
***
5.自定义登录成功逻辑  
实现与配置AuthenticationSuccessHandler
***
6.获取认证请求和用户信息  
SecurityContextHolder
***
7.自定义登录失败逻辑  
实现与配置AuthenticationFailureHandler  
***
8.添加图形验证过滤器
OncePerRequestFilter 
