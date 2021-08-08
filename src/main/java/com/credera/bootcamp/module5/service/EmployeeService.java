package com.credera.bootcamp.module5.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService<T> {

    T create(T employee);

    T retrieveOneByName(String firstName, String lastName) throws EntityNotFoundException;

    T updateEmployee(T employee);

    void deleteEmployee(long employeeId);

    List<T> findByLastName(String lastName);

    List<T> findByLastName(String lastName, Sort sort);

    Page<T> findByLastName(String lastName, Pageable pageable);

    List<T> findAll();

    T findById(long employeeId);

}
