package com.backend.demo.controller;

import com.backend.demo.domain.BackendArticle;
import com.backend.demo.po.Article;
import com.backend.demo.po.User;
import com.backend.demo.service.ArticleService;
import com.backend.demo.service.UserService;
import com.backend.demo.util.BackendUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/controller")
public class AccountController {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @GetMapping("/userId/{account}")
    public Long getUserId(@PathVariable String account) {
        return userService.getUserByAccount(account).getId();
    }

    @GetMapping("/user/{id}")
    public Map<String, String> getUser(@PathVariable String id) {

        Map<String, String> map = new HashMap<>();

        User user = userService.getUser(Long.parseLong(id, 10));

        map.put("username", user.getAccount());
        map.put("interests", "打球、睡覺");
        map.put("gender", user.getGender());
        map.put("level", user.getLevel()) ;

        return map ;
    }

    @GetMapping("/accountTotalPage/{userId}")
    public Integer totalPage(@PathVariable String userId) {
        return articleService.getUserArticle(userService.getUser(Long.parseLong(userId, 10))).size();
    }

    @GetMapping("/account/{id}/{page}")
    public Page<BackendArticle> account(@PathVariable String id,
                                        @PathVariable Integer page) {

        Pageable pageable = PageRequest.of(page, 3);
        List<BackendArticle> list = new ArrayList<>();

        Page<Article> pageArticle =
                articleService.getPageArticleByUser(userService.getUser(Long.parseLong(id, 10)), pageable);

        for (Article article : pageArticle) {
            list.add(BackendUtils.articleToBackendArticle(article,
                    ((Number) redisTemplate.opsForValue().get("article" + article.getId())).longValue()));
        }

        Page<BackendArticle> pageList = new PageImpl<>(list);

        return pageList ;
    }
}
