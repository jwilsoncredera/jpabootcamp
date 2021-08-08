package com.credera.bootcamp.module5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credera.bootcamp.module5.model.Practice;
import com.credera.bootcamp.module5.repository.PracticeRepository;

@Service
public class PracticeServiceImpl implements PracticeService {

    @Autowired
    protected PracticeRepository practiceRepository;

    @Override
    public List<Practice> findByLongNameLike(final String longName) {
        return practiceRepository.findByLongNameLike("%" + longName + "%");
    }

    @Override
    public List<Practice> findAll() {
        return practiceRepository.findAll();
    }

}
