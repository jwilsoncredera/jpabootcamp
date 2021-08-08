package com.credera.bootcamp.module5.practice;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.credera.bootcamp.module5.model.Practice;
import com.credera.bootcamp.module5.service.PracticeServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestPracticeServiceImpl {

    @Autowired
    protected PracticeServiceImpl service;

    @Test
    public void testFindByShortName() {
        // search for seed data
        final String shortName = "OTS";

        // tested method
        Practice practice = service.findByShortName(shortName);

        // verification
        Assert.assertEquals(shortName, practice.getShortName());
    }

    @Test
    public void testFindByLongNameLike() {
        // search for seed data
        final String longNameFragment = "Open";

        // tested method
        List<Practice> practices = service.findByLongNameLike(longNameFragment);

        // verification
        Assert.assertEquals(1, practices.size());
        Assert.assertEquals("OTS", practices.get(0).getShortName());
        Assert.assertTrue(practices.get(0).getLongName().contains(longNameFragment));
    }

    @Test
    public void testFindAll() {
        // tested method
        List<Practice> employees = service.findAll();

        // verification
        Assert.assertTrue(employees.size() > 2);
    }

}
