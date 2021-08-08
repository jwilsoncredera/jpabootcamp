package com.credera.bootcamp.module5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credera.bootcamp.module5.dao.PracticeDao;
import com.credera.bootcamp.module5.dto.PracticeDto;

@Service
public class DaoBasedPracticeService implements PracticeService<PracticeDto> {

    @Autowired
    protected PracticeDao practiceDao;

    @Override
    public PracticeDto findByShortName(final String shortName) {
        return practiceDao.findByShortName(shortName);
    }

    @Override
    public List<PracticeDto> findByLongNameLike(final String longName) {
        return practiceDao.findByLongNameLike(longName);
    }

    @Override
    public List<PracticeDto> findAll() {
        return practiceDao.findAll();
    }

}
