package com.backend.demo.domain;

import lombok.Data;


@Data
public class BackendArticle {

    private Long id ;
    private String title ;
    private String content ;
    private Long views ;
    private String updateTime ;
    private BackendUser User ;
    private BackendType type ;

}
