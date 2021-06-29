package com.backend.demo.service;

import com.backend.demo.dao.CommentRepository;
import com.backend.demo.domain.BackendComment;
import com.backend.demo.po.Comment;
import com.backend.demo.util.BackendUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository ;

    @Autowired
    private ArticleService articleService;

    @Override
    public List<BackendComment> getCommentsByArticle(Long articleId) {
        List<Comment> comments =
                commentRepository.findAllByArticle(articleService.getArticle(articleId));

        List<BackendComment> list = new ArrayList<>();

        for (Comment comment : comments) {
            list.add(BackendUtils.commentToBackendComment(comment));
        }

        return list;
    }

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }


}
