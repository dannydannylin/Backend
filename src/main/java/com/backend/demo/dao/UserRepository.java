package com.backend.demo.dao;

import com.backend.demo.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByAccountAndPassword(String account, String password);

    User findByAccount(String account) ;

    List<User> findAllByLevelEquals(String level) ;

    List<User> findAllByAccountLike(String query) ;

}
