package com.backend.demo.util;

import com.backend.demo.po.Article;
import com.backend.demo.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class MyBeanUtils {

    public static void updateArticle(Article target, Article source) {
        // 更新時只會更新文章標題和內容
        // 也可以換版
        target.setTitle(source.getTitle());
        target.setContent(source.getContent());
        target.setType(source.getType());
    }

    public static String typeToName(Integer typeId) {
        if ( typeId == Type.INTERNATIONAL ) {
            return "國際版" ;
        }
        else if ( typeId == Type.MUSIC ) {
            return "音樂版" ;
        }
        else if ( typeId == Type.SPORTS ) {
            return "體育版" ;
        }
        return "" ;
    }

    // 把 list 轉成 page
    public static Page<Article> listToPage(Pageable pageable, List<Article> articles) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), articles.size());
        Page<Article> page = new PageImpl<>(articles.subList(start, end), pageable, articles.size());
        return page ;
    }

}
