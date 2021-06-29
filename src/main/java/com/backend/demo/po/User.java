package com.backend.demo.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue
    private Long id ;
    private String account ;
    private String password ;
    private String email ;
    private String gender ;
    private String avatar ;
    private String level ;
    private boolean block ;
    private boolean post ;
    private boolean comment ;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime ;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime ;
    @OneToMany(mappedBy = "user")
    private List<Article> articles = new ArrayList<>() ;
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    private String friends ;

}
