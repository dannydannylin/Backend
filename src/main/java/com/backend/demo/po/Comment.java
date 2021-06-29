package com.backend.demo.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "t_comment")
public class Comment {

    @Id
    @GeneratedValue
    private Long id ;
    private String content ;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime ;
    @ManyToOne()
    private Article article;
    @ManyToOne()
    private User user ;

    // 留言再留言　先暫時不做　!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyComments = new ArrayList<>() ;
    @ManyToOne
    private Comment parentComment ;


}
