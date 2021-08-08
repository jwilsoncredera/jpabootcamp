package com.credera.bootcamp.module5.dto;

public class EmployeeDto extends AuditableDto {

    private Long id;

    private String firstName;

    private String lastName;

    private Long practiceId;

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

    public Long getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(final Long practiceId) {
        this.practiceId = practiceId;
    }

}
