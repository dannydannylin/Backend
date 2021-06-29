package com.backend.demo.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {

    private String message ;
    private String time ;
    private Long absoluteTime ;
    private String direction;
    private boolean read ;
}
