package com.backend.demo.service;

import com.backend.demo.po.Article;
import com.backend.demo.po.Type;
import com.backend.demo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {

    Article getArticle(Long id) ;

    Article addArticle(Article article) ;

    Article updateArticle(Long id, Article article) ;

    void deleteArticle(Long id) ;

    List<Article> getAllArticle() ;

    List<Article> getUserArticle(User id) ;

    Page<Article> getPageArticle(Pageable pageable) ;

    Page<Article> getPageArticleByType(Type type, Pageable pageable) ;

    List<Article> getAllByType(Type type) ;

    Page<Article> getPageArticleByUser(User user, Pageable pageable);

    Page<Article> getPageArticleBySearch(String query1, String query2, Pageable pageable);

    List<Article> getAllArticleBySearch(String query, String query2);

}
