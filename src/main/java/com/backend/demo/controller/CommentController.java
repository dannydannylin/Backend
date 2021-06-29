package com.backend.demo.controller;

import com.backend.demo.domain.BackendComment;
import com.backend.demo.po.Comment;
import com.backend.demo.service.ArticleService;
import com.backend.demo.service.CommentService;
import com.backend.demo.service.UserService;
import com.backend.demo.util.BackendUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/controller")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/comments/{id}")
    public List<BackendComment> getComments(@PathVariable String id) {

        return commentService.getCommentsByArticle(Long.parseLong(id, 10));
    }

    @PostMapping("/comments")
    public boolean addComment(@RequestBody BackendComment backendComment) {

        Comment comment =
                BackendUtils.backendCommentToComment(backendComment, userService, articleService);
        commentService.addComment(comment);
        return true ;
    }

    @DeleteMapping("/comments/{id}")
    public boolean deleteComment(@PathVariable String id) {
        commentService.deleteComment(Long.parseLong(id, 10));

        return true ;
    }
}
