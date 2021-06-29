package com.backend.demo.controller;

import com.backend.demo.po.User;
import com.backend.demo.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/controller")
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/verify")
    public boolean verify(@RequestBody String jsonToken) {

        JSONObject jsonObject = new JSONObject(jsonToken) ;
        String account = jsonObject.getString("account");

        if ( account.equals("") ) {
            return false ;
        }

        User user = userService.containUserAccount(account);
        if ( user == null ) {
            return true;
        }
        else {
            return false;
        }
    }

    @PostMapping("/register")
    public boolean register(@RequestBody User user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        // 一般註冊的帳號都只給 user 權限
        user.setLevel("user");
        // 不封鎖
        user.setBlock(false);
        // 可發文
        user.setPost(true);
        // 可留言
        user.setComment(true);

        userService.addUser(user);
        return true ;
    }
}
