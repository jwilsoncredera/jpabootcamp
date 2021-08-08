package com.credera.bootcamp.module5.employee;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hibernate.LazyInitializationException;
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

import com.credera.bootcamp.module5.model.Employee;
import com.credera.bootcamp.module5.service.EmployeeServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestEmployeeServiceImpl {

    @Autowired
    protected EmployeeServiceImpl service;

    @Test
    public void testContextLoads() {
    }

    @Test
    public void testCreateEmployee() {
        // setup
        final String firstName = "TheFName";
        final String lastName = "TheLName";
        final Employee employeeToMake = new Employee();
        employeeToMake.setFirstName(firstName);
        employeeToMake.setLastName(lastName);

        // tested method
        service.create(employeeToMake);

        // verification
        final List<Employee> employees = service.findByLastName(lastName);
        Assert.assertEquals(1, employees.size());
        final Employee employee = employees.get(0);
        Assert.assertEquals(firstName, employee.getFirstName());
        Assert.assertEquals(lastName, employee.getLastName());
        Assert.assertNull(employee.getPractice());
    }

    @Test
    public void testRetrieveEmployee() {
        // search for seed data
        final String searchedFirstName = "Justin";
        final String searchedLastName = "Wilson";

        // tested method
        Employee employee = service.retrieveOneByName(searchedFirstName, searchedLastName);

        // verification
        Assert.assertNotNull(employee);
        Assert.assertEquals(searchedFirstName, employee.getFirstName());
        Assert.assertEquals(searchedLastName, employee.getLastName());
        Assert.assertNotNull(employee.getPractice());
    }
    
    @Test
    public void testUpdateEmployee() {
        // setup
        final String oldFirstName = "Foo";
        final String oldLastName = "Bar";
        final Employee employeeToMake = new Employee();
        employeeToMake.setFirstName(oldFirstName);
        employeeToMake.setLastName(oldLastName);
        final Employee createdEmployee = service.create(employeeToMake);
        final Long createdEmployeeId = createdEmployee.getId();

        // verify setup
        Employee employeeToUpdate = service.retrieveOneByName(oldFirstName, oldLastName);
        Assert.assertEquals(createdEmployeeId, employeeToUpdate.getId());

        final String updatedFirstName = "Bax";
        final String updatedLastName = "Xab";
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
        Employee updatedEmployee = service.retrieveOneByName(updatedFirstName, updatedLastName);
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

    @Test
    public void testDeleteEmployee() {
        // setup
        final String firstName = "Foo";
        final String lastName = "Bar";
        final Employee employeeToMake = new Employee();
        employeeToMake.setFirstName(firstName);
        employeeToMake.setLastName(lastName);
        final Employee createdEmployee = service.create(employeeToMake);
        final Long createdEmployeeId = createdEmployee.getId();

        // verify setup
        Employee retrievedEmployee = service.retrieveOneByName(firstName, lastName);
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

    @Test
    public void testUpdatePracticeThroughEmployee() {
        // search for seed data
        final String searchedFirstName = "Justin";
        final String searchedLastName = "Wilson";
        final String newPracticeLongName = "Test";

        // remember old practice name
        Employee employee = service.retrieveOneByName(searchedFirstName, searchedLastName);
        final Long employeeId = employee.getId();
        final String oldPracticeLongName = employee.getPractice().getLongName();

        // tested method - update database
        service.updatePracticeThroughEmployee(employeeId, newPracticeLongName);

        // verify update persisted
        employee = service.retrieveOneByName(searchedFirstName, searchedLastName);
        Assert.assertEquals(newPracticeLongName, employee.getPractice().getLongName());

        // undo update (to avoid impacting future tests)
        service.updatePracticeThroughEmployee(employeeId, oldPracticeLongName);
    }

    @Test(expected = LazyInitializationException.class)
    public void testFetchPracticeEmployeesWithoutTransaction() {
        // search for seed data
        final String searchedFirstName = "Justin";
        final String searchedLastName = "Wilson";

        // remember old practice name
        Employee employee = service.retrieveOneByName(searchedFirstName, searchedLastName);

        List<Employee> employees = employee.getPractice().getEmployees();
        employees.get(0); // will throw a LazyInitializationException
    }

    @Test
    public void testTransactionallyUpdateEmployee() {
        // setup
        final String firstName = "Foo";
        final String oldLastName = "Bar";
        final Employee employeeToMake = new Employee();
        employeeToMake.setFirstName(firstName);
        employeeToMake.setLastName(oldLastName);
        final Employee createdEmployee = service.create(employeeToMake);
        final Long createdEmployeeId = createdEmployee.getId();

        // tested method
        final String newLastName = "Transactional";
        service.updateLastNameThroughTransaction(createdEmployeeId, newLastName);

        // verification
        Employee retrievedEmployee = service.retrieveOneByName(firstName, newLastName);
        Assert.assertEquals(createdEmployeeId, retrievedEmployee.getId());
    }

    @Test
    public void testTransactionalRollback() {
        // setup
        final String firstName = "Zoo";
        final String oldLastName = "Zar";
        final Employee employeeToMake = new Employee();
        employeeToMake.setFirstName(firstName);
        employeeToMake.setLastName(oldLastName);
        final Employee createdEmployee = service.create(employeeToMake);
        final Long createdEmployeeId = createdEmployee.getId();

        // tested method
        boolean exceptionFound = false;
        final String newLastName = "Transactional";
        try {
            service.updateTwoLastNamesThroughTransactions(createdEmployeeId, newLastName, -99L, "second will fail");
        } catch (EntityNotFoundException e) {
            exceptionFound = true;
        }

        // verification
        Assert.assertTrue(exceptionFound);
        // throwing an exception while saving the second edit rolled back the first edit
        Employee retrievedEmployee = service.retrieveOneByName(firstName, oldLastName);
        Assert.assertEquals(createdEmployeeId, retrievedEmployee.getId());
    }

    @Test
    public void testFindWithSort() {
        // https://www.baeldung.com/spring-data-sorting

        // search for seed data
        final String searchedLastName = "Triplet";
        final Sort sort = Sort.by(Sort.Direction.DESC, "firstName");

        // tested method
        List<Employee> employees = service.findByLastName(searchedLastName, sort);

        // verification
        Assert.assertEquals(3, employees.size());
        Assert.assertEquals("3rd", employees.get(0).getFirstName());
        Assert.assertEquals("2nd", employees.get(1).getFirstName());
        Assert.assertEquals("1st", employees.get(2).getFirstName());
    }

    @Test
    public void testFindPage() {
        // https://www.baeldung.com/spring-data-sorting

        // search for seed data
        final String searchedLastName = "Triplet";
        final Sort sort = Sort.by(Sort.Direction.ASC, "firstName");

        // tested method
        Pageable pageable = PageRequest.of(0, 2, sort); // first page
        Page<Employee> employeesPage = service.findByLastName(searchedLastName, pageable);

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

    @Test
    public void testFindAll() {
        // tested method
        List<Employee> employees = service.findAll();

        // verification
        Assert.assertTrue(employees.size() > 2);
    }

    @Test
    public void testById() {
        Long seedEmployeeId = 1L;

        // tested method
        Employee employee = service.findById(seedEmployeeId);

        // verification
        Assert.assertEquals(seedEmployeeId, employee.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testByIdNotFound() {
        // tested method
        service.findById(-999L);
    }

}
