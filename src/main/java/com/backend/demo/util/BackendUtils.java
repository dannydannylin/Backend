package com.backend.demo.util;


import com.backend.demo.domain.*;
import com.backend.demo.po.Article;
import com.backend.demo.po.Comment;
import com.backend.demo.po.Type;
import com.backend.demo.po.User;
import com.backend.demo.service.ArticleService;
import com.backend.demo.service.UserService;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BackendUtils {

    public static BackendArticle articleToBackendArticle(Article article, Long views) {

        Calendar calender = Calendar.getInstance();
        calender.setTime(article.getUpdateTime());

        BackendArticle backendArticle = new BackendArticle();
        backendArticle.setId(article.getId());
        backendArticle.setTitle(article.getTitle());
        backendArticle.setContent(article.getContent());


        // backendArticle.setViews(article.getViews());
        backendArticle.setViews(views);

        backendArticle.setUpdateTime(calender.get(Calendar.YEAR) + "-" + ( calender.get(Calendar.MONTH)+1 ) + "-" + calender.get(Calendar.DATE));
        backendArticle.setUser(userToBackendUser(article.getUser()));
        backendArticle.setType(typeToBackendType(article.getType()));

        return backendArticle ;
    }

    public static BackendUser userToBackendUser(User user) {
        BackendUser backendUser = new BackendUser();
        backendUser.setId(user.getId());
        backendUser.setPassword(user.getPassword());
        backendUser.setAccount(user.getAccount());
        backendUser.setLevel(user.getLevel());
        backendUser.setBlock(user.isBlock());
        backendUser.setPost(user.isPost());
        backendUser.setComment(user.isComment());
        return backendUser ;
    }

    private static BackendType typeToBackendType(Type type) {
        BackendType Backendtype = new BackendType();
        Backendtype.setId(type.getId());
        Backendtype.setName(type.chineseName());
        return Backendtype ;
    }

    public static Article backendArticleToArticle(BackendArticle backendArticle) {
        Article article = new Article();
        article.setTitle(backendArticle.getTitle());
        article.setContent(backendArticle.getContent());
        article.setViews(backendArticle.getViews());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date() ;
        try {
            date = sdf.parse(backendArticle.getUpdateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        article.setUpdateTime(date);

        return article ;
    }

    public static BackendComment commentToBackendComment(Comment comment) {
        BackendComment backendComment = new BackendComment();
        backendComment.setId(comment.getId());
        backendComment.setArticleId(comment.getArticle().getId());
        backendComment.setContent(comment.getContent());
        backendComment.setUpdateTime(comment.getCreateTime().toString());
        backendComment.setUser(userToBackendUser(comment.getUser()));

        return backendComment ;
    }


    public static Comment backendCommentToComment(BackendComment backendComment,
                                                  UserService userService,
                                                  ArticleService articleService) {

        BackendUser user = backendComment.getUser();

        User realUser = userService.getUserByAccount(user.getAccount());

        Comment comment = new Comment();
        comment.setUser(realUser);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date() ;
        try {
            date = sdf.parse(backendComment.getUpdateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        comment.setCreateTime(date);

        comment.setArticle(articleService.getArticle(backendComment.getArticleId()));
        comment.setContent(backendComment.getContent());

        return comment;
    }

}
