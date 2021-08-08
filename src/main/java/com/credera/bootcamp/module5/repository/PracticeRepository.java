package com.credera.bootcamp.module5.repository;

import java.util.List;

import com.credera.bootcamp.module5.model.Practice;

public interface PracticeRepository {

    public List<Practice> findByLongNameLike(String longName);

}
