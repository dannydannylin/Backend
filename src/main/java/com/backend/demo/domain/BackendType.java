package com.backend.demo.domain;

import lombok.Data;

@Data
public class BackendType {
    private Long id ;
    private String name ;

    // 修改 name
    // 讓正確的 name 對應到正確的 id
    public void modify() {
        if ( this.name.equals("SPORTS") ) {
            this.id = 1l ;
        }
        else if ( this.name.equals("MUSIC") ) {
            this.id = 2l ;
        }
        else if ( this.name.equals("INTERNATIONAL") ) {
            this.id = 3l ;
        }
    }

}
