package com.credera.bootcamp.module5.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Component;

import com.credera.bootcamp.module5.dto.EmployeeDto;
import com.credera.bootcamp.module5.model.Employee;
import com.credera.bootcamp.module5.model.Practice;

@Component
public class EmployeeDao {

    @PersistenceContext(unitName = "default")
    protected EntityManager em;

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto dto) {
        Employee employee = new Employee();
        populateEmployeeFromDto(employee, dto);

        em.persist(employee);

        return convertEmployeeToDto(employee);
    }

    public EmployeeDto findEmployeeById(long employeeId) {
        Employee employee = em.find(Employee.class, employeeId);

        return convertEmployeeToDto(employee);
    }

    @Transactional
    public EmployeeDto updateEmployee(EmployeeDto dto) {
        Employee employee = em.find(Employee.class, dto.getId());
        populateEmployeeFromDto(employee, dto);

        Employee updatedEmployee = em.merge(employee);

        return convertEmployeeToDto(updatedEmployee);
    }

    @Transactional
    public void deleteEmployee(long employeeId) {
        Employee employee = em.find(Employee.class, employeeId);
        em.remove(employee);
    }

    public EmployeeDto findOneByFirstNameAndLastNameOrderByIdDesc(String firstName, String lastName) {
        TypedQuery<Employee> query = em
                .createNamedQuery("FIND_BY_FIRST_NAME_AND_LAST_NAME_ORDER_BY_ID_DESC", Employee.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        query.setHint("org.hibernate.cacheable", false);
        query.setMaxResults(1);
        Employee employee = query.getSingleResult();

        return convertEmployeeToDto(employee);
    }

    public List<EmployeeDto> findByLastName(String lastName) {
        TypedQuery<Employee> query = em.createQuery("SELECT employee FROM " + Employee.class.getName() + " employee"
                + " WHERE employee.lastName = :lastName", Employee.class);
        query.setParameter("lastName", lastName);
        List<Employee> employees = query.getResultList();

        return convertEmployeesToDtos(employees);
    }

    public List<EmployeeDto> findByLastName(String lastName, Sort sort) {
        // Another way to construct queries
        // See https://www.baeldung.com/jpa-sort
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);
        Root<Employee> root = criteria.from(Employee.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("lastName"), lastName));

        criteria.orderBy(QueryUtils.toOrders(sort, root, builder));

        TypedQuery<Employee> query = em.createQuery(criteria);
        List<Employee> employees = query.getResultList();

        return convertEmployeesToDtos(employees);
    }

    public Page<EmployeeDto> findByLastName(String lastName, Pageable pageable) {
        // https://www.baeldung.com/jpa-pagination
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);
        Root<Employee> root = criteria.from(Employee.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("lastName"), lastName));

        // Extract sort
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));

        TypedQuery<Employee> query = em.createQuery(criteria);
        // Page criteria
        query.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Employee> employees = query.getResultList();

        // Count total
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        countCriteria.select(builder.count(countCriteria.from(Employee.class)));
        countCriteria.where(builder.equal(root.get("lastName"), lastName));
        Long count = em.createQuery(countCriteria).getSingleResult();

        List<EmployeeDto> employeeDtos = convertEmployeesToDtos(employees);
        Page<EmployeeDto> page = new PageImpl<>(employeeDtos, pageable, count);
        return page;
    }

    public List<EmployeeDto> findAll() {
        // Another way to construct queries
        // See https://www.baeldung.com/jpa-sort
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);
        Root<Employee> root = criteria.from(Employee.class);
        criteria.select(root);

        TypedQuery<Employee> query = em.createQuery(criteria);
        List<Employee> employees = query.getResultList();

        return convertEmployeesToDtos(employees);
    }

    private EmployeeDto convertEmployeeToDto(Employee employee) {
        EmployeeDto dto = null;
        if (employee != null) {
            dto = new EmployeeDto();
            dto.setId(employee.getId());
            dto.setFirstName(employee.getFirstName());
            dto.setLastName(employee.getLastName());
            if (employee.getPractice() != null) {
                dto.setPracticeId(employee.getPractice().getId());
            }
            dto.setCreatedDate(employee.getCreatedDate());
            dto.setUpdatedDate(employee.getUpdatedDate());
        }
        return dto;
    }

    private void populateEmployeeFromDto(Employee employee, EmployeeDto dto) {
        // Not setting ID, as it's auto-generated

        if (dto.getFirstName() != null) {
            employee.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            employee.setLastName(dto.getLastName());
        }
        if (dto.getPracticeId() != null) {
            // Retrieving a Managed Entity
            Practice practice = em.find(Practice.class, dto.getPracticeId());
            employee.setPractice(practice);
        }
        if (dto.getCreatedDate() != null) {
            employee.setCreatedDate(dto.getCreatedDate());
        }
        if (dto.getUpdatedDate() != null) {
            employee.setUpdatedDate(dto.getUpdatedDate());
        }
    }

    private List<EmployeeDto> convertEmployeesToDtos(Collection<Employee> employees) {
        List<EmployeeDto> retVal;
        if (employees == null || employees.size() == 0) {
            retVal = new ArrayList<>(0);
        } else {
            retVal = employees.stream().map(employeeResult -> convertEmployeeToDto(employeeResult))
                    .collect(Collectors.toList());
        }
        return retVal;
    }
}
