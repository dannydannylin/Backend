package com.backend.demo.service;

import com.backend.demo.dao.TypeRepository;
import com.backend.demo.po.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository ;


    @Override
    public Type getTypeById(Long id) {
        return typeRepository.findById(id).orElse(null);
    }
}
