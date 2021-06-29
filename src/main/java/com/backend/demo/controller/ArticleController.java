package com.backend.demo.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.backend.demo.domain.BackendArticle;
import com.backend.demo.po.Article;
import com.backend.demo.service.ArticleService;
import com.backend.demo.service.TypeService;
import com.backend.demo.service.UserService;
import com.backend.demo.util.BackendUtils;
import com.backend.demo.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/controller")
public class ArticleController {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private TypeService typeService;

    @GetMapping("/article/{id}")
    public BackendArticle getArticle(@PathVariable String id) {

        Article article = articleService.getArticle(Long.parseLong(id, 10));

        Long views = ((Number) redisTemplate.opsForValue().get("article" + article.getId())).longValue() ;

        if ( article == null ) {
            return null ;
        }

        BackendArticle backendArticle = BackendUtils.articleToBackendArticle(article, views);

        return backendArticle ;
    }

    @PostMapping("/article")
    public boolean postArticle(@RequestBody BackendArticle backendArticle) {

        // 這邊只有設定基本性質
        Article article = BackendUtils.backendArticleToArticle(backendArticle);
        // 新增創建時間
        article.setCreateTime(new Date());

        article.setUser(userService.getUserByAccount(backendArticle.getUser().getAccount()));
        article.setType(typeService.getTypeById(backendArticle.getType().getId()));

        articleService.addArticle(article);


        // redis 發新的文章
        List<Article> allArticle = articleService.getAllArticle();
        // 找剛剛存的新文章的 id 是多少
        // 存到 redis 中
        redisTemplate.opsForValue().set( "article" +
                allArticle.get(allArticle.size() - 1).getId(), 0l );

        return true ;
    }

    // ============== 這兩個方法需要權限 ==============

    @DeleteMapping("/article/{id}")
    public boolean deleteArticle(@PathVariable String id,
                                 @RequestHeader("token") String token) {

        try {
            // 驗證使用戶合法
            JwtUtils.verify(token);
            DecodedJWT tokenInfo = JwtUtils.getTokenInfo(token);

            // 此用戶是發文用戶
            // 或是他是板主
            // 或他是神
            if ( tokenInfo.getClaim("account").asString().
                    equals(articleService.getArticle(Long.parseLong(id, 10)).getUser().getAccount())
                    || tokenInfo.getClaim("level").asString().equals("master")
                    || tokenInfo.getClaim("level").asString().equals("boss") ) {

                // 也要移除 redis 中儲存的 views
                redisTemplate.delete("article"+ id) ;

                articleService.deleteArticle(Long.parseLong(id, 10));
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

    @PutMapping("/article/{id}")
    public boolean postArticle(@PathVariable String id,
                               @RequestBody BackendArticle backendArticle,
                               @RequestHeader("token") String token) {

        try {
            JwtUtils.verify(token);
            DecodedJWT tokenInfo = JwtUtils.getTokenInfo(token);

            if ( tokenInfo.getClaim("account").asString().
                    equals(articleService.getArticle(Long.parseLong(id, 10)).getUser().getAccount())
                    || tokenInfo.getClaim("level").asString().equals("master")
                    || tokenInfo.getClaim("level").asString().equals("boss") ) {
                // 這邊只有設定基本性質
                Article article = BackendUtils.backendArticleToArticle(backendArticle);
                article.setUser(userService.getUser(backendArticle.getUser().getId()));
                article.setType(typeService.getTypeById(backendArticle.getType().getId()));
                articleService.updateArticle(Long.parseLong(id, 10), article);
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

    // ============== 這兩個方法需要權限 ==============


    @GetMapping("/article/addViews/{id}")
    public boolean addViews(@PathVariable String id) {

        // 使用 redis 增加 views
        redisTemplate.opsForValue().increment("article" + id) ;

        // 不要使用 mysql 增加 views
        // 因為太消耗性能

//        Article article = articleService.getArticle(Long.parseLong(id, 10));
//        article.setViews(article.getViews()+1);
//        articleService.updateArticle(Long.parseLong(id, 10), article);

        return true ;
    }

}
