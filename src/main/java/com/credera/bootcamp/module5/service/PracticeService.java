package com.credera.bootcamp.module5.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface PracticeService<T> {

    T findByShortName(String shortName);

    List<T> findByLongNameLike(String longName);

    List<T> findAll();

}
