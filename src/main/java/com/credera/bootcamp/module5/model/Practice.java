package com.credera.bootcamp.module5.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "practice")
public class Practice extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "short_name", unique = true, nullable = false, length = 3)
    public String shortName;

    @Column(name = "long_name")
    public String longName;

    /**
     * Lazily loaded upon request within a transaction
     */
    public List<Employee> employees;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(final String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(final String longName) {
        this.longName = longName;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(final List<Employee> employees) {
        this.employees = employees;
    }

}
