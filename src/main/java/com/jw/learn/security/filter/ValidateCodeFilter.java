package com.jw.learn.security.filter;

import com.jw.learn.security.controller.ValidateController;
import com.jw.learn.security.entity.ImageCode;
import com.jw.learn.security.exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ValidateCodeFilter extends OncePerRequestFilter {  //该过滤器只会执行一次

  @Autowired private AuthenticationFailureHandler authenticationFailureHandler;

  private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {
    if (StringUtils.equalsIgnoreCase("/login", httpServletRequest.getRequestURI())
        && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {
      try {
        validateCode(new ServletWebRequest(httpServletRequest));
      } catch (ValidateCodeException e) {
        authenticationFailureHandler.onAuthenticationFailure(
            httpServletRequest, httpServletResponse, e);
        return;
      }
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

  private void validateCode(ServletWebRequest servletWebRequest)
      throws ServletRequestBindingException, ValidateCodeException {
    ImageCode codeInSession =
        (ImageCode)
            sessionStrategy.getAttribute(
                servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
    String codeInRequest =
        ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");

    if (StringUtils.isBlank(codeInRequest)) {
      throw new ValidateCodeException("验证码不能为空！");
    }
    if (codeInSession == null) {
      throw new ValidateCodeException("验证码不存在！");
    }
    if (codeInSession.isExpire()) {
      sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
      throw new ValidateCodeException("验证码已过期！");
    }
    if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
      throw new ValidateCodeException("验证码不正确！");
    }
    sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
  }
}
