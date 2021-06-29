package com.backend.demo.admin.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.backend.demo.domain.BackendUser;
import com.backend.demo.po.User;
import com.backend.demo.service.UserService;
import com.backend.demo.util.BackendUtils;
import com.backend.demo.util.JwtUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/controller/admin/boss")
public class BossAuth {

    @Autowired
    private UserService userService ;

    private boolean verify(String token) {
        try {
            JwtUtils.verify(token);
            DecodedJWT tokenInfo = JwtUtils.getTokenInfo(token);

            User account = userService.getUserByAccount(tokenInfo.getClaim("account").asString());

            if ( account.getLevel().equals("boss")) {
                return true ;
            }
            else {
                return false ;
            }
        }
        catch (Exception e) {
        }
        return false ;
    }

    @GetMapping("/users")
    public List<BackendUser> getUsers(@RequestHeader("token") String token) {

        if ( !this.verify(token) ) {
            return null ;
        }

        List<User> allUser = userService.getAllUser();
        List<BackendUser> list = new ArrayList<>();

        for (User user : allUser) {
            list.add(BackendUtils.userToBackendUser(user)) ;
        }

        return list;
    }

    @PostMapping("/users/{id}")
    public boolean changeAuth(@PathVariable String id, @RequestBody String data,
                              @RequestHeader("token") String token) {

        if ( !this.verify(token) ) {
            return false ;
        }

        JSONObject jsonObject = new JSONObject(data) ;
        String level = jsonObject.getString("level");

        User user = userService.getUser(Long.parseLong(id, 10));

        // 神不可以修改權限
        if ( user.getLevel().equals("boss") ) {
            return false ;
        }

        user.setLevel(level);
        userService.updateUser(user);
        return true ;
    }

    @DeleteMapping("/users/{id}")
    public boolean blockUser(@PathVariable String id,
                             @RequestHeader("token") String token) {

        if ( !this.verify(token) ) {
            return false ;
        }

        User user = userService.getUser(Long.parseLong(id, 10));

        // 神不可以被封鎖
        if ( user.getLevel().equals("boss") ) {
            return false ;
        }

        user.setBlock(true);
        userService.updateUser(user) ;
        return true ;
    }

    @PutMapping("/users/{id}")
    public boolean unBlockUser(@PathVariable String id,
                               @RequestHeader("token") String token) {

        if ( !this.verify(token) ) {
            return false ;
        }

        User user = userService.getUser(Long.parseLong(id, 10));
        user.setBlock(false);
        userService.updateUser(user) ;
        return true ;
    }

}
