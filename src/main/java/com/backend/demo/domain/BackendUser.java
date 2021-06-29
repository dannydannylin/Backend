package com.backend.demo.domain;

import lombok.Data;

@Data
public class BackendUser {
    private Long id ;
    private String account ;
    private String password ;
    private String level ;
    private boolean block ;
    private boolean post;
    private boolean comment ;
}
