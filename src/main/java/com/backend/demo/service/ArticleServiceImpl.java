package com.backend.demo.service;

import com.backend.demo.NotFoundException;
import com.backend.demo.dao.ArticleRepository;
import com.backend.demo.po.Article;
import com.backend.demo.po.Type;
import com.backend.demo.po.User;
import com.backend.demo.util.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository ;

    @Override
    public Article getArticle(Long id) {
        return articleRepository.findById(id).orElse(null) ;
    }

    @Transactional
    @Override
    public Article addArticle(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    @Override
    public Article updateArticle(Long id, Article article) {
        Article one = articleRepository.getOne(id) ;
        if ( one == null ) {
            throw new NotFoundException("沒有此文章") ;
        }
        // 更新文章標題和內容
        MyBeanUtils.updateArticle(one, article);
        return articleRepository.save(one);
    }

    @Transactional
    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<Article> getAllArticle() {
        return articleRepository.findAll();
    }

    @Override
    @Transactional
    public List<Article> getUserArticle(User user) {
        return articleRepository.findAllByUser(user);
    }

    @Override
    public Page<Article> getPageArticle(Pageable pageable) {

        return articleRepository.findAll(pageable);
    }

    @Override
    public Page<Article> getPageArticleByType(Type type, Pageable pageable) {
        return articleRepository.findAllByType(type, pageable);
    }

    @Override
    public List<Article> getAllByType(Type type) {
        return articleRepository.findAllByType(type) ;
    }

    @Override
    public Page<Article> getPageArticleByUser(User user, Pageable pageable) {
        return articleRepository.findAllByUser(user, pageable);
    }

    @Override
    public Page<Article> getPageArticleBySearch(String query1, String query2,Pageable pageable) {
        return articleRepository.
                findAllByTitleLikeOrContentLike("%"+ query1 + "%", "%"+ query2 + "%",pageable);
    }

    @Override
    public List<Article> getAllArticleBySearch(String query1, String query2) {
        return articleRepository.
                findAllByTitleLikeOrContentLike("%"+ query1 + "%", "%"+ query2 + "%");
    }

}
