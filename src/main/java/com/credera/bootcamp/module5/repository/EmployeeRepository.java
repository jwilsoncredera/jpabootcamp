package com.credera.bootcamp.module5.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.credera.bootcamp.module5.model.Employee;

/**
 * Using Spring Data allows us to avoid manually creating queries
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Return first one even if there are more matches
     */
    Employee findOneByFirstNameAndLastNameOrderByIdDesc(String firstName, String lastName);

    /**
     * Return all matches
     * </p>
     * We don't have to use @Query here, it's just present as an example.
     * If it were gone, Spring Data would automatically create the query by reflexively inspecting the method name.
     */
    @Query("SELECT employee FROM Employee employee WHERE employee.lastName = :lastName")
    List<Employee> findByLastName(String lastName);

    /**
     * Sort
     */
    List<Employee> findByLastName(String lastName, Sort sort);

    /**
     * Page and sort
     */
    Page<Employee> findByLastName(String lastName, Pageable pageable);

}
