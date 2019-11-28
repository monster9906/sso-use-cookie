package com.sso.ssologin.controller;

import com.sso.ssologin.poji.User;
import com.sso.ssologin.utils.LoginCacheUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/view")
public class ViewController {

    @RequestMapping("/login")
    public String toLogin(@RequestParam (required = false,defaultValue = "") String target, HttpSession session,@CookieValue(required = false,value = "TOKEN") Cookie cookie){
        if(StringUtils.isEmpty(target)){
            // 没有地址就跳转到首页
            target = "http://www.codeshop.com:8889/view/index";
        }
        if(cookie != null){
            String token = cookie.getValue();
            User user = LoginCacheUtil.loginUser.get(token);
            if(user != null){
                // 说明用户已经登录过，不需要再次访问，注解跳转页面
                return "redirect:"+target;
            }
        }
        // 存放跳转前的页面地址
        session.setAttribute("target", target);
        return "login";
    }
}
