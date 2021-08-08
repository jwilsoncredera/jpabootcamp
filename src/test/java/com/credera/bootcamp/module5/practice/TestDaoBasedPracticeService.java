package com.credera.bootcamp.module5.practice;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.credera.bootcamp.module5.dto.PracticeDto;
import com.credera.bootcamp.module5.service.DaoBasedPracticeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestDaoBasedPracticeService implements CommonPracticeTests {

    @Autowired
    protected DaoBasedPracticeService service;

    @Override
    @Test
    public void testFindByShortName() {
        // search for seed data
        final String shortName = "OTS";

        // tested method
        PracticeDto practice = service.findByShortName(shortName);

        // verification
        Assert.assertEquals(shortName, practice.getShortName());
    }

    @Override
    @Test
    public void testFindByLongNameLike() {
        // search for seed data
        final String longNameFragment = "Open";

        // tested method
        List<PracticeDto> practices = service.findByLongNameLike(longNameFragment);

        // verification
        Assert.assertEquals(1, practices.size());
        Assert.assertEquals("OTS", practices.get(0).getShortName());
        Assert.assertTrue(practices.get(0).getLongName().contains(longNameFragment));
    }

    @Override
    @Test
    public void testFindAll() {
        // tested method
        List<PracticeDto> employees = service.findAll();

        // verification
        Assert.assertTrue(employees.size() > 2);
    }

}
