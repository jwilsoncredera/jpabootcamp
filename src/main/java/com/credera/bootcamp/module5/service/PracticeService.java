package com.credera.bootcamp.module5.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.credera.bootcamp.module5.model.Practice;

@Service
public interface PracticeService {

    Practice findByShortName(String shortName);

    List<Practice> findByLongNameLike(String longName);

    List<Practice> findAll();

}
