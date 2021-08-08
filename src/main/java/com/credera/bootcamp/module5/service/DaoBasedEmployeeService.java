package com.credera.bootcamp.module5.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.credera.bootcamp.module5.dao.EmployeeDao;
import com.credera.bootcamp.module5.dto.EmployeeDto;

@Service
public class DaoBasedEmployeeService implements EmployeeService<EmployeeDto> {

    @Autowired
    protected EmployeeDao employeeDao;

    @Override
    public EmployeeDto create(final EmployeeDto employee) {
        return employeeDao.createEmployee(employee);
    }

    @Override
    public EmployeeDto retrieveOneByName(final String firstName, String lastName) throws EntityNotFoundException {
        try {
            return employeeDao.findOneByFirstNameAndLastNameOrderByIdDesc(firstName, lastName);
        } catch (NoResultException e) {
            final String message = EmployeeDao.class.getName() + " not found for name: " + firstName + " " + lastName;
            throw new EntityNotFoundException(message);
        }
    }

    @Override
    public EmployeeDto updateEmployee(final EmployeeDto employee) {
        return employeeDao.updateEmployee(employee);
    }

    @Override
    public void deleteEmployee(final long employeeId) {
        employeeDao.deleteEmployee(employeeId);
    }

    @Override
    public List<EmployeeDto> findByLastName(final String lastName) {
        return employeeDao.findByLastName(lastName);
    }

    @Override
    public List<EmployeeDto> findByLastName(final String lastName, final Sort sort) {
        return employeeDao.findByLastName(lastName, sort);
    }

    @Override
    public Page<EmployeeDto> findByLastName(final String lastName, final Pageable pageable) {
        return employeeDao.findByLastName(lastName, pageable);
    }

    @Override
    public List<EmployeeDto> findAll() {
        return employeeDao.findAll();
    }

    @Override
    public EmployeeDto findById(final long employeeId) {
        EmployeeDto employeeDto = employeeDao.findEmployeeById(employeeId);
        if (employeeDto == null) {
            final String message = EmployeeDao.class.getName() + " not found for id: " + employeeId;
            throw new EntityNotFoundException(message);
        }
        return employeeDto;
    }

}
