package com.sso.ssomain.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/view")
public class ViewController {
    @Autowired
    private RestTemplate restTemplate;
    private final String LOGIN_ADDRESS = "http://login.codeshop.com:8888/login/info?token=";

    @GetMapping("/index")
    public String toIndex(@CookieValue(required = false, value= "TOKEN") Cookie cookie, HttpSession session) throws IOException {
        if(cookie != null){
            //获取Cookie
            String token = cookie.getValue();
            System.out.println(token);
            if(! StringUtils.isEmpty(token)){
                // 发送请求
                //ResponseEntity<User> forEntity = restTemplate.getForEntity( LOGIN_ADDRESS+ token, User.class);
                //User user = forEntity.getBody();
                // 创建httpclient对象
                CloseableHttpClient httpClient = HttpClients.createDefault();
                // 创建get方式请求对象
                HttpGet httpGet = new HttpGet(LOGIN_ADDRESS+token);
                httpGet.addHeader("Content-type", "application/json");
                // 通过请求对象获取响应对象
                CloseableHttpResponse response = httpClient.execute(httpGet);
                //获取结果实体
                // 判断网络连接状态码是否正常(0--200都数正常)
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String s = EntityUtils.toString(response.getEntity(), "utf-8");
                    System.out.println("响应数据"+JSONObject.parseObject(s));
                    session.setAttribute("loginUser", JSONObject.parseObject(s));
                }
                // session.setAttribute("loginUser", s);
                // 释放链接
                response.close();
            }
        }
       return "index";
    }
}
