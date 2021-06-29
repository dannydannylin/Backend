package com.backend.demo.service;

import com.backend.demo.domain.BackendComment;
import com.backend.demo.po.Comment;

import java.util.List;

public interface CommentService {

    List<BackendComment> getCommentsByArticle(Long articleId) ;

    Comment addComment(Comment comment) ;

    void deleteComment(Long commentId) ;

}
