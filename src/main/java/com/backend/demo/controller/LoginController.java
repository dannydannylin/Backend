package com.backend.demo.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.backend.demo.domain.BackendUser;
import com.backend.demo.domain.LoginResult;
import com.backend.demo.po.User;
import com.backend.demo.service.UserService;
import com.backend.demo.util.JwtUtils;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/controller")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public LoginResult login(@RequestBody BackendUser backendUser) {

        LoginResult loginResult = new LoginResult();

        User user = userService.checkUser(backendUser.getAccount(), backendUser.getPassword());

        // 登入失敗
        if ( user == null || user.isBlock() ) {
            loginResult.setStateCode(400);
            return loginResult ;
        }
        // 登入成功
        else {
            // 把要回傳的訊息包裝起來
            Map<String, String> payload = new HashMap<>();
            payload.put("id", user.getId().toString());
            payload.put("account", user.getAccount()) ;
            payload.put("level", user.getLevel());

            loginResult.setStateCode(200);
            loginResult.setToken(JwtUtils.getToken(payload));
            return loginResult ;
        }
    }

    @PostMapping("/isToken")
    public boolean isToken(@RequestBody String jsonToken) {
        // 為什麼不用密碼 ??
        // 這有點像是 我曾經登入成功
        // 登入成功後給我一個識別證
        // 以後只要拿出識別證 就知道是合法可以登入的用戶
        // 而這個識別證不容易偽造
        JSONObject jsonObject = new JSONObject(jsonToken) ;
        String token = jsonObject.getString("token");

        try {

            JwtUtils.verify(token);
            DecodedJWT tokenInfo = JwtUtils.getTokenInfo(token);
            String id = tokenInfo.getClaim("id").asString();
            long ID = Long.parseLong(id, 10);
            User user = userService.getUser(ID);

            if ( user.isBlock() ) {
                log.info("此為被封鎖帳號");
                return false ;
            }
            else {
                log.info("驗證成功");
                return true ;
            }

        }catch (Exception e) {
            log.info("驗證失敗");
        }
        return false ;
    }

    @PostMapping("/userInfo")
    public Map<String, Object> getUserInfo(@RequestBody String jsonToken) {
        JSONObject jsonObject = new JSONObject(jsonToken) ;
        String token = jsonObject.getString("token");
        DecodedJWT tokenInfo = JwtUtils.getTokenInfo(token);

        Map<String, Object> map = new HashMap<>();
        String account = tokenInfo.getClaim("account").asString();
        map.put("account", account) ;

        // 權限者種東西可能會被管理員修改
        // 被需重新重資料庫讀取
        // 且 post 和 comment 根本就沒有加到 token 裡
        User userByAccount = userService.getUserByAccount(account);

        map.put("level", userByAccount.getLevel()) ;
        map.put("post", userByAccount.isPost() ) ;
        map.put("comment", userByAccount.isComment()) ;
        return map;
    }

    @GetMapping("/hi")
    public String hi() {
        return "Hi Azure ~~~~~~" ;
    }

}
