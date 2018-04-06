package com.juez.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Code entity.
 */
public class CodeDTO implements Serializable {

    private Long id;

    private String name;

    private ZonedDateTime dateupload;

    private Long submissionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDateupload() {
        return dateupload;
    }

    public void setDateupload(ZonedDateTime dateupload) {
        this.dateupload = dateupload;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CodeDTO codeDTO = (CodeDTO) o;
        if(codeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), codeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CodeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateupload='" + getDateupload() + "'" +
            "}";
    }
}
