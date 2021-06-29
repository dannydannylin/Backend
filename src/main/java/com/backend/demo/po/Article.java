package com.backend.demo.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_article")
@NoArgsConstructor
@ToString
@Data
public class Article {

    @Id
    @GeneratedValue
    private Long id ;
    private String title ;
    private String content ;
    private String picture ;
    private Long views ;
    private boolean published ;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime ;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime ;

    @ManyToOne
    private Type type ;
    @ManyToOne
    private User user ;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>() ;

}
