package com.sso.ssologin.controller;

import com.alibaba.fastjson.JSON;
import com.sso.ssologin.dao.UserDao;
import com.sso.ssologin.poji.User;
import com.sso.ssologin.utils.LoginCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/login")
public class LoginController {
    // TODO 此处忽略service层的代码处理
    @Autowired
    private UserDao userDao;

    /**执行登录操作
     *
     * @param session
     * @param response
     * @param username
     * @param password
     * @return
     */
    @PostMapping()
    public String doLogin(HttpSession session, HttpServletResponse response, String username, String password){
        String target = (String) session.getAttribute("target");
        User user = userDao.findByUsernameAndAndPassword(username, password);
        if(user == null){ // 登录出错
            session.setAttribute("msg", "账号或密码错误");
            return "login";
        }else {
            // 保存登录信息
            String token = UUID.randomUUID().toString();
            System.out.println("登录时存放的值："+token);
            Cookie cookie = new Cookie("TOKEN", token);
            cookie.setDomain("codeshop.com");
            // 响应cookie
            response.addCookie(cookie);
            // 记录缓存信息
            LoginCacheUtil.loginUser.put(token, user);
        }
        return "redirect:"+target;
    }

    /**
     * 获取缓存的用户信息
     * @param token
     * @return
     */
    @GetMapping("/info")
    @ResponseBody
    public String getUserInfo(String token){
        if(StringUtils.isEmpty(token)){
            /*new ResponseEntity<>(null, HttpStatus.BAD_REQUEST)*/
           return null;
        }else {
            User user = LoginCacheUtil.loginUser.get(token);
           // ResponseEntity<User> ok = ResponseEntity.ok(user);
            //String s = JSONObject.toJSONString(user);
            return JSON.toJSONString(user);
        }
    }


}
