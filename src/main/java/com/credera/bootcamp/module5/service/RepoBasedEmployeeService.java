package com.credera.bootcamp.module5.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.credera.bootcamp.module5.model.Employee;
import com.credera.bootcamp.module5.model.Practice;
import com.credera.bootcamp.module5.repository.EmployeeRepository;

@Service
public class RepoBasedEmployeeService implements EmployeeService<Employee> {

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Override
    public Employee create(final Employee employee) {
        // Same as update, except the source data is not a Managed Entity
        return employeeRepository.save(employee);
    }

    @Override
    public Employee retrieveOneByName(final String firstName, String lastName) throws EntityNotFoundException {
        Employee retVal = employeeRepository.findOneByFirstNameAndLastNameOrderByIdDesc(firstName, lastName);
        if (retVal == null) {
            final String message = Employee.class.getName() + " not found for name: " + firstName + " " + lastName;
            throw new EntityNotFoundException(message);
        }
        return retVal;
    }

    @Override
    public Employee updateEmployee(final Employee employee) {
        // Same as create, except the source data is a Managed Entity
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(final long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public List<Employee> findByLastName(final String lastName) {
        return employeeRepository.findByLastName(lastName);
    }

    @Override
    public List<Employee> findByLastName(final String lastName, final Sort sort) {
        return employeeRepository.findByLastName(lastName, sort);
    }

    @Override
    public Page<Employee> findByLastName(final String lastName, final Pageable pageable) {
        return employeeRepository.findByLastName(lastName, pageable);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(final long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) {
            final String message = Employee.class.getName() + " not found for id: " + employeeId;
            throw new EntityNotFoundException(message);
        }
        return optionalEmployee.get();
    }

    @Transactional
    public void updatePracticeThroughEmployee(final Long employeeId, final String newPracticeLongName) {
        Employee employee = findById(employeeId);
        Practice practice = employee.getPractice(); // no need for practiceRepository

        practice.setLongName(newPracticeLongName);
        // exiting the transaction will automatically save the change to Practice
    }

    /**
     * This will fail unless the caller uses a transaction
     */
    public void updatePracticeThroughEmployeeNoTransaction(final Long employeeId, final String newPracticeLongName) {
        Employee employee = findById(employeeId);
        Practice practice = employee.getPractice(); // no need for practiceRepository

        practice.setLongName(newPracticeLongName);
        // exiting the transaction will automatically save the change to Practice
    }

    @Transactional
    public void updateLastNameThroughTransaction(final Long employeeId, final String newLastName) {
        Employee employee = findById(employeeId);

        employee.setLastName(newLastName);
        // exiting the transaction will automatically save the change to Employee
    }

    @Transactional
    public void updateTwoLastNamesThroughTransactions(final Long employeeId1, final String newLastName1,
            final Long employeeId2, final String newLastName2) {
        updateLastNameThroughTransaction(employeeId1, newLastName1);
        updateLastNameThroughTransaction(employeeId2, newLastName2);
    }

}
