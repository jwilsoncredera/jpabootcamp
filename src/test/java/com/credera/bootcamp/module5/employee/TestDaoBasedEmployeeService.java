package com.credera.bootcamp.module5.employee;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.credera.bootcamp.module5.dto.EmployeeDto;
import com.credera.bootcamp.module5.service.DaoBasedEmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestDaoBasedEmployeeService implements CommonEmployeeTests {

    @Autowired
    protected DaoBasedEmployeeService service;

    @Autowired
    protected PlatformTransactionManager transactionManager;

    @Test
    public void testContextLoads() {
    }

    @Override
    @Test
    public void testCreateEmployee() {
        // setup
        final String firstName = "A_FName";
        final String lastName = "A_LName";
        final EmployeeDto employeeToMake = new EmployeeDto();
        employeeToMake.setFirstName(firstName);
        employeeToMake.setLastName(lastName);

        // tested method
        service.create(employeeToMake);

        // verification
        final List<EmployeeDto> employees = service.findByLastName(lastName);
        Assert.assertEquals(1, employees.size());
        final EmployeeDto employee = employees.get(0);
        Assert.assertEquals(firstName, employee.getFirstName());
        Assert.assertEquals(lastName, employee.getLastName());
        Assert.assertNull(employee.getPracticeId());
    }

    @Override
    @Test
    public void testRetrieveEmployee() {
        // search for seed data
        final String searchedFirstName = "Justin";
        final String searchedLastName = "Wilson";

        // tested method
        EmployeeDto employee = service.retrieveOneByName(searchedFirstName, searchedLastName);

        // verification
        Assert.assertNotNull(employee);
        Assert.assertEquals(searchedFirstName, employee.getFirstName());
        Assert.assertEquals(searchedLastName, employee.getLastName());
        Assert.assertNotNull(employee.getPracticeId());
    }

    @Override
    @Test
    public void testUpdateEmployee() {
        // setup
        final String oldFirstName = "Foo";
        final String oldLastName = "Bar";
        final EmployeeDto employeeToMake = new EmployeeDto();
        employeeToMake.setFirstName(oldFirstName);
        employeeToMake.setLastName(oldLastName);
        final EmployeeDto createdEmployee = service.create(employeeToMake);
        final Long createdEmployeeId = createdEmployee.getId();

        // verify setup
        EmployeeDto employeeToUpdate = service.retrieveOneByName(oldFirstName, oldLastName);
        Assert.assertEquals(createdEmployeeId, employeeToUpdate.getId());

        final String updatedFirstName = "Baz";
        final String updatedLastName = "Zab";
        employeeToUpdate.setFirstName(updatedFirstName);
        employeeToUpdate.setLastName(updatedLastName);

        boolean exceptionOccurred = false;
        try {
            service.retrieveOneByName(updatedFirstName, updatedLastName);
        } catch (EntityNotFoundException e) {
            exceptionOccurred = true;
        }
        Assert.assertTrue(exceptionOccurred);

        // tested method
        service.updateEmployee(employeeToUpdate);

        // verification
        EmployeeDto updatedEmployee = service.retrieveOneByName(updatedFirstName, updatedLastName);
        Assert.assertEquals(createdEmployeeId, updatedEmployee.getId());
        Assert.assertEquals(updatedFirstName, updatedEmployee.getFirstName());
        Assert.assertEquals(updatedLastName, updatedEmployee.getLastName());

        exceptionOccurred = false;
        try {
            service.retrieveOneByName(oldFirstName, oldLastName);
        } catch (EntityNotFoundException e) {
            exceptionOccurred = true;
        }
        Assert.assertTrue(exceptionOccurred);
    }

    @Override
    @Test
    public void testDeleteEmployee() {
        // setup
        final String firstName = "Foo";
        final String lastName = "Bar";
        final EmployeeDto employeeToMake = new EmployeeDto();
        employeeToMake.setFirstName(firstName);
        employeeToMake.setLastName(lastName);
        final EmployeeDto createdEmployee = service.create(employeeToMake);
        final Long createdEmployeeId = createdEmployee.getId();

        // verify setup
        EmployeeDto retrievedEmployee = service.retrieveOneByName(firstName, lastName);
        Assert.assertEquals(createdEmployeeId, retrievedEmployee.getId());

        // tested method
        service.deleteEmployee(createdEmployeeId);

        // verification
        boolean exceptionOccurred = false;
        try {
            service.retrieveOneByName(firstName, lastName);
        } catch (EntityNotFoundException e) {
            exceptionOccurred = true;
        }
        Assert.assertTrue(exceptionOccurred);
    }

    @Override
    @Test
    public void testTransactionallyUpdateEmployee() {
        // setup
        final String firstName = "Moo";
        final String oldLastName = "Mar";
        final EmployeeDto employeeToMake = new EmployeeDto();
        employeeToMake.setFirstName(firstName);
        employeeToMake.setLastName(oldLastName);
        final EmployeeDto createdEmployee = service.create(employeeToMake);
        final Long createdEmployeeId = createdEmployee.getId();

        final String newLastName = "Transactional";

        // alternative to using @Transactional
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        final EmployeeDto updatedEmployee = transactionTemplate.execute(new TransactionCallback<EmployeeDto>() {
            @Override
            public EmployeeDto doInTransaction(TransactionStatus transactionStatus) {
                // verify setup
                EmployeeDto employeeToUpdate = service.retrieveOneByName(firstName, oldLastName);
                Assert.assertEquals(createdEmployeeId, employeeToUpdate.getId());

                // tested method
                employeeToUpdate.setLastName(newLastName);
                return service.updateEmployee(employeeToUpdate);
            }
        });

        // verification
        EmployeeDto retrievedEmployee = service.retrieveOneByName(firstName, newLastName);
        Assert.assertEquals(createdEmployeeId, retrievedEmployee.getId());
        Assert.assertEquals(updatedEmployee.getId(), retrievedEmployee.getId());
        Assert.assertEquals(newLastName, retrievedEmployee.getLastName());
    }

    @Override
    @Test
    public void testTransactionalRollback() {
        // setup
        final String firstName = "Goo";
        final String oldLastName = "Gar";
        final EmployeeDto employeeToMake = new EmployeeDto();
        employeeToMake.setFirstName(firstName);
        employeeToMake.setLastName(oldLastName);
        final EmployeeDto createdEmployee = service.create(employeeToMake);
        final Long createdEmployeeId = createdEmployee.getId();

        boolean exceptionFound = false;
        try {
            // alternative to using @Transactional
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                public void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    // verify setup
                    EmployeeDto employeeToUpdate = service.retrieveOneByName(firstName, oldLastName);
                    Assert.assertEquals(createdEmployeeId, employeeToUpdate.getId());

                    // tested method
                    final String newLastName = "Transactional";
                    employeeToUpdate.setLastName(newLastName);
                    service.updateEmployee(employeeToUpdate);

                    throw new EntityNotFoundException("test");
                }
            });
        } catch (EntityNotFoundException e) {
            exceptionFound = true;
        }

        // verification
        Assert.assertTrue(exceptionFound);
        // throwing an exception while saving the second edit rolled back the first edit
        EmployeeDto retrievedEmployee = service.retrieveOneByName(firstName, oldLastName);
        Assert.assertEquals(createdEmployeeId, retrievedEmployee.getId());
    }

    @Override
    @Test
    public void testFindWithSort() {
        // https://www.baeldung.com/spring-data-sorting

        // search for seed data
        final String searchedLastName = "Triplet";
        final Sort sort = Sort.by(Sort.Direction.DESC, "firstName");

        // tested method
        List<EmployeeDto> employees = service.findByLastName(searchedLastName, sort);

        // verification
        Assert.assertEquals(3, employees.size());
        Assert.assertEquals("3rd", employees.get(0).getFirstName());
        Assert.assertEquals("2nd", employees.get(1).getFirstName());
        Assert.assertEquals("1st", employees.get(2).getFirstName());
    }

    @Override
    @Test
    public void testFindPage() {
        // https://www.baeldung.com/spring-data-sorting

        // search for seed data
        final String searchedLastName = "Triplet";
        final Sort sort = Sort.by(Sort.Direction.ASC, "firstName");

        // tested method
        Pageable pageable = PageRequest.of(0, 2, sort); // first page
        Page<EmployeeDto> employeesPage = service.findByLastName(searchedLastName, pageable);

        // verification
        Assert.assertEquals(3, employeesPage.getTotalElements());
        Assert.assertEquals(2, employeesPage.getContent().size());
        Assert.assertEquals("1st", employeesPage.getContent().get(0).getFirstName());
        Assert.assertEquals("2nd", employeesPage.getContent().get(1).getFirstName());

        // tested method
        pageable = PageRequest.of(1, 2, sort); // second page
        employeesPage = service.findByLastName(searchedLastName, pageable);

        // verification
        Assert.assertEquals(3, employeesPage.getTotalElements());
        Assert.assertEquals(1, employeesPage.getContent().size());
        Assert.assertEquals("3rd", employeesPage.getContent().get(0).getFirstName());
    }

    @Override
    @Test
    public void testFindAll() {
        // tested method
        List<EmployeeDto> employees = service.findAll();

        // verification
        Assert.assertTrue(employees.size() > 2);
    }

    @Override
    @Test
    public void testById() {
        Long seedEmployeeId = 1L;

        // tested method
        EmployeeDto employee = service.findById(seedEmployeeId);

        // verification
        Assert.assertEquals(seedEmployeeId, employee.getId());
    }

    @Override
    @Test(expected = EntityNotFoundException.class)
    public void testByIdNotFound() {
        // tested method
        service.findById(-999L);
    }

}
