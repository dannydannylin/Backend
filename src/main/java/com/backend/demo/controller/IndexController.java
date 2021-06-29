package com.backend.demo.controller;

import com.backend.demo.domain.BackendArticle;
import com.backend.demo.po.Article;
import com.backend.demo.po.Type;
import com.backend.demo.service.ArticleService;
import com.backend.demo.service.UserService;
import com.backend.demo.util.BackendUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/controller")
public class IndexController {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @GetMapping("/totalPage/{typeId}")
    public Integer totalPage(@PathVariable String typeId) {

        // 首頁
        if ( typeId.equals("0") ) {
            return articleService.getAllArticle().size();
        }
        // sports
        else if ( typeId.equals("1") ) {
            return articleService.getAllByType(new Type(1l)).size();
        }
        // music
        else if ( typeId.equals("2") ) {
            return articleService.getAllByType(new Type(2l)).size();
        }
        // international
        else if ( typeId.equals("3") ) {
            return articleService.getAllByType(new Type(3l)).size();
        }
        else {
            return articleService.getUserArticle(userService.getUser(Long.parseLong(typeId, 10))).size();
        }

    }

    private Page<BackendArticle> getByType(Type type, Integer page) {

        Pageable pageable = PageRequest.of(page, 3);

        List<BackendArticle> list = new ArrayList<>();
        Page<Article> allArticle = articleService.getPageArticleByType(type, pageable) ;

        for (Article article : allArticle) {
            list.add(BackendUtils.articleToBackendArticle(article,
                    ((Number) redisTemplate.opsForValue().get("article" + article.getId())).longValue() ));
        }

        Page<BackendArticle> pageList = new PageImpl<>(list);

        return pageList ;
    }

    @GetMapping("/index/{page}")
    public Page<BackendArticle> index(@PathVariable String page) {

        System.out.println(Integer.parseInt(page));

        Pageable pageable = PageRequest.of(Integer.parseInt(page), 3);

        Page<Article> allArticle = articleService.getPageArticle(pageable) ;
        List<BackendArticle> list = new ArrayList<>();

        for (Article article : allArticle) {
            list.add(BackendUtils.articleToBackendArticle(article,
                    ((Number) redisTemplate.opsForValue().get("article" + article.getId())).longValue() ));
        }

        Page<BackendArticle> pageList = new PageImpl<>(list);

        return pageList ;
    }

    @GetMapping("/sports/{page}")
    public Page<BackendArticle> sports(@PathVariable String page) {
        return this.getByType(new Type( new Long( Type.SPORTS ) ),Integer.parseInt(page)) ;
    }

    @GetMapping("/music/{page}")
    public Page<BackendArticle> music(@PathVariable String page) {
        return this.getByType(new Type( new Long( Type.MUSIC ) ),Integer.parseInt(page)) ;
    }

    @GetMapping("/international/{page}")
    public Page<BackendArticle> international(@PathVariable String page) {
        return this.getByType(new Type( new Long( Type.INTERNATIONAL ) ),Integer.parseInt(page)) ;
    }

    // ===================== 搜尋結果 =====================

    @PostMapping("/search/{page}")
    public Page<BackendArticle> search(@PathVariable String page,
                                       @RequestBody String Text) {

        JSONObject jsonObject = new JSONObject(Text) ;
        String searchText = jsonObject.getString("searchText");

        Pageable pageable = PageRequest.of(Integer.parseInt(page), 3);

        List<BackendArticle> list = new ArrayList<>();
        Page<Article> allArticle = articleService.getPageArticleBySearch(searchText, searchText,pageable) ;

        for (Article article : allArticle) {
            list.add(BackendUtils.articleToBackendArticle(article,
                    ((Number) redisTemplate.opsForValue().get("article" + article.getId())).longValue() ));
        }

        Page<BackendArticle> pageList = new PageImpl<>(list);

        return pageList ;
    }

    @PostMapping("/searchTotalPage")
    public Integer searchTotalPage(@RequestBody String Text) {
        JSONObject jsonObject = new JSONObject(Text) ;
        String searchText = jsonObject.getString("searchText");
        return articleService.getAllArticleBySearch(searchText,searchText).size();
    }
}
