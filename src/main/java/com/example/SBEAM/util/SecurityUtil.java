package com.example.SBEAM.util;

import com.example.SBEAM.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: DingXiaoyu
 * @Date: 0:28 2023/11/26
 * 你可以通过这个类的方法来获得当前用户的信息。
*/
@Component
public class SecurityUtil {

    @Autowired
    HttpServletRequest httpServletRequest;

    public User getCurrentUser(){
        return (User)httpServletRequest.getSession().getAttribute("currentUser");
    }
}
