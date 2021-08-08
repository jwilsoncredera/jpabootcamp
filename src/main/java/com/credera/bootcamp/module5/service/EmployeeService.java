package com.credera.bootcamp.module5.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.credera.bootcamp.module5.model.Employee;

@Service
public interface EmployeeService {

    Employee create(Employee employee);

    Employee retrieveOneByName(String firstName, String lastName) throws EntityNotFoundException;

    Employee updateEmployee(Employee employee);

    void deleteEmployee(long employeeId);

    List<Employee> findByLastName(String lastName);

    List<Employee> findByLastName(String lastName, Sort sort);

    Page<Employee> findByLastName(String lastName, Pageable pageable);

    List<Employee> findAll();

    Employee findById(long employeeId);

}
