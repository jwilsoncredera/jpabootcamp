package com.credera.bootcamp.module5.model;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class Employee extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "first_name", nullable = false)
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @ManyToOne(targetEntity = Practice.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "practice_id")
    public Practice practice;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public Practice getPractice() {
        return practice;
    }

    public void setPractice(final Practice practice) {
        this.practice = practice;
    }

}
