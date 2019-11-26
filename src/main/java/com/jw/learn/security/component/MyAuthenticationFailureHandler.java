package com.jw.learn.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Autowired private ObjectMapper mapper;

    /**
     * AuthenticationException:
     * 不同的失败原因对应不同的异常，
     * 比如用户名或密码错误对应的是BadCredentialsException，
     * 用户不存在对应的是UsernameNotFoundException，用户被锁定对应的是LockedException等
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     */
  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.setContentType("application/json;charset=utf-8");
    response.getWriter().write(mapper.writeValueAsString(exception.getMessage()));
  }
}
