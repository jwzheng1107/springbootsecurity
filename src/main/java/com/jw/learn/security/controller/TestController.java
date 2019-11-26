package com.jw.learn.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("hello")
  public String hello() {
    return "hello spring security";
  }

    /**
     * 2种获取Authentication的方式
     * @param authentication
     * @return
     */
  //  @GetMapping("index")
  //  public Object index() {
  //    return SecurityContextHolder.getContext().getAuthentication();
  //  }

  @GetMapping("index")
  public Object index(Authentication authentication) {
    return authentication;
  }
}
