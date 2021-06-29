package com.backend.demo.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Friend implements Serializable {

    private String account ;
    private String lastChat ;
    private String lastChatTime ;
    private Long lastAbsoluteTime ;
    private List<Message> messages ;

}
