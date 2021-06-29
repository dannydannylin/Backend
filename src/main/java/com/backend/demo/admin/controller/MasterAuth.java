package com.backend.demo.admin.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.backend.demo.domain.BackendUser;
import com.backend.demo.po.User;
import com.backend.demo.service.UserService;
import com.backend.demo.util.BackendUtils;
import com.backend.demo.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/controller/admin/master")
public class MasterAuth {

    @Autowired
    private UserService userService ;

    private boolean verify(String token) {
        try {
            JwtUtils.verify(token);
            DecodedJWT tokenInfo = JwtUtils.getTokenInfo(token);

            User account = userService.getUserByAccount(tokenInfo.getClaim("account").asString());

            if ( account.getLevel().equals("master")) {
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

        List<User> allUser = userService.getAllUserByLevel("user");
        List<BackendUser> list = new ArrayList<>();

        for (User user : allUser) {
            list.add(BackendUtils.userToBackendUser(user)) ;
        }

        return list;
    }

    @DeleteMapping("/userPost/{id}")
    public boolean blockUserPost(@PathVariable String id,
                                 @RequestHeader("token") String token) {

        if ( !this.verify(token) ) {
            return false ;
        }

        User user = userService.getUser(Long.parseLong(id, 10));
        user.setPost(false);
        userService.updateUser(user) ;
        return true ;
    }

    @PutMapping("/userPost/{id}")
    public boolean unBlockUserPost(@PathVariable String id,
                                   @RequestHeader("token") String token) {
        if ( !this.verify(token) ) {
            return false ;
        }

        User user = userService.getUser(Long.parseLong(id, 10));
        user.setPost(true);
        userService.updateUser(user) ;
        return true ;
    }

    @DeleteMapping("/userComment/{id}")
    public boolean blockUserComment(@PathVariable String id,
                                    @RequestHeader("token") String token) {

        if ( !this.verify(token) ) {
            return false ;
        }

        User user = userService.getUser(Long.parseLong(id, 10));
        user.setComment(false);
        userService.updateUser(user) ;
        return true ;
    }

    @PutMapping("/userComment/{id}")
    public boolean unBlockUserComment(@PathVariable String id,
                                      @RequestHeader("token") String token) {

        if ( !this.verify(token) ) {
            return false ;
        }

        User user = userService.getUser(Long.parseLong(id, 10));
        user.setComment(true);
        userService.updateUser(user) ;
        return true ;
    }

}
