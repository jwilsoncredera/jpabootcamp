package com.credera.bootcamp.module5.model;

import javax.persistence.*;

public class Employee extends Auditable {

    public Long id;

    public String firstName;

    public String lastName;

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
