package com.backend.demo.dao;

import com.backend.demo.po.Article;
import com.backend.demo.po.Type;
import com.backend.demo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findAllByType(Type type) ;

    List<Article> findAllByUser(User user) ;

    Page<Article> findAllByType(Type type, Pageable pageable) ;

    Page<Article> findAllByUser(User user, Pageable pageable) ;

    Page<Article> findAllByTitleLikeOrContentLike(String query1, String query2, Pageable pageable) ;

    List<Article> findAllByTitleLikeOrContentLike(String query, String query2) ;

}
