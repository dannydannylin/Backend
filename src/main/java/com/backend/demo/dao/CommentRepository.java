package com.backend.demo.dao;

import com.backend.demo.po.Article;
import com.backend.demo.po.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByArticle(Article article);


}
