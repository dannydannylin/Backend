package com.backend.demo.service;

import com.backend.demo.dao.UserRepository;
import com.backend.demo.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository ;

    @Transactional
    @Override
    public User checkUser(String account, String password) {
        User user = userRepository.findByAccountAndPassword(account, password );
        return user ;
    }

    @Transactional
    @Override
    public User addUser(User user) {
        user.setPassword( user.getPassword() );
        return userRepository.save( user );
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User containUserAccount(String account) {
        return userRepository.findByAccount(account);
    }

    @Override
    public User getUserByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public List<User> getAllUserByLevel(String level) {
        return userRepository.findAllByLevelEquals(level);
    }

    @Override
    public List<User> getAllUserBySearch(String query) {
        return userRepository.findAllByAccountLike(query);
    }

}
