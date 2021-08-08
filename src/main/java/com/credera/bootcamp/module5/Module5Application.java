package com.credera.bootcamp.module5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.credera.bootcamp.module5.service.DaoBasedPracticeService;
import com.credera.bootcamp.module5.service.RepoBasedEmployeeService;

@SpringBootApplication
public class Module5Application implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(Module5Application.class);

    @Autowired
    private RepoBasedEmployeeService repoBasedEmployeeService;

    @Autowired
    private DaoBasedPracticeService daoBasedPracticeService;

    public static void main(String[] args) {
        SpringApplication.run(Module5Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Debugging methods when running application outside of tests
        logger.debug("************ Start debugging queries ************");
        repoBasedEmployeeService.findAll()
                .forEach(employee -> logger.debug(employee.getFirstName() + " " + employee.getLastName()));
        daoBasedPracticeService.findAll()
                .forEach(practiceDto -> logger.debug(practiceDto.getShortName() + ": " + practiceDto.getLongName()));
        logger.debug("************ End debugging queries ************");
    }

}
