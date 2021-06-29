package com.backend.demo.util;

public class PageTypeUtils {

    public static Long pageConvert(String pageType) {

        if ( pageType.equals("sports") ) {
            return new Long(1) ;
        }
        else if ( pageType.equals("music") ) {
            return new Long(2) ;
        }
        else if ( pageType.equals("international") ) {
            return new Long(3) ;
        }
        return  null ;
    }
}
