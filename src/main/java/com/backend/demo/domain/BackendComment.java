package com.backend.demo.domain;

import lombok.Data;

@Data
public class BackendComment {

    private Long id ;
    private Long articleId ;
    private BackendUser user;
    private String content;
    private String updateTime ;
}
