package com.credera.bootcamp.module5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.credera.bootcamp.module5.model.Practice;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {

    public Practice findByShortName(String shortName);

    public List<Practice> findByLongNameLike(String longName);

}
