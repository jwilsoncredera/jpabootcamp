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
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    protected EmployeeRepository employeeRepository;

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

    public void updatePracticeThroughEmployee(final Long employeeId, final String newPracticeLongName) {
        // TODO: Change the practice long name without calling any save methods
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
