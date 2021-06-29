package com.backend.demo.service;


import com.backend.demo.po.User;

import java.util.List;

public interface UserService {
    User checkUser(String account, String password) ;

    User addUser(User user) ;

    User getUser(Long userId) ;

    User containUserAccount(String account) ;

    User getUserByAccount(String account) ;

    List<User> getAllUser() ;

    User updateUser(User user);

    List<User> getAllUserByLevel(String level) ;

    List<User> getAllUserBySearch(String query) ;

}
