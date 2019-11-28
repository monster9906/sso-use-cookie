package com.sso.ssologin.utils;

import com.sso.ssologin.poji.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录缓存信息
 */
public class LoginCacheUtil {
    public static Map<String, User> loginUser  = new HashMap<>();
}
