package com.backend.demo.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
@Data
@Entity
@Table(name = "t_type")
public class Type {

    public static final Integer SPORTS = 1 ;
    public static final Integer MUSIC = 2 ;
    public static final Integer INTERNATIONAL = 3 ;

    @Id
    @GeneratedValue
    private Long id ;
    private String name ;

    @OneToMany(mappedBy = "type")
    private List<Article> articles = new ArrayList<>() ;

    public Type(Long id) {
        this.id = id ;
    }

    public String chineseName() {
        if ( this.getId() == 1 ) {
            return "體育" ;
        }
        else if ( this.getId() == 2 ) {
            return "音樂" ;
        }
        else if ( this.getId() == 3 ) {
            return "國際" ;
        }

        return "" ;
    }
}

