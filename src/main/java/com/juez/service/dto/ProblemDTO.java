package com.juez.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Problem entity.
 */
public class ProblemDTO implements Serializable {

    private Long id;

    private String name;

    private Integer timelimit;

    private String pdflocation;

    private Long solutionId;

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

    public Integer getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(Integer timelimit) {
        this.timelimit = timelimit;
    }

    public String getPdflocation() {
        return pdflocation;
    }

    public void setPdflocation(String pdflocation) {
        this.pdflocation = pdflocation;
    }

    public Long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(Long codeId) {
        this.solutionId = codeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProblemDTO problemDTO = (ProblemDTO) o;
        if(problemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), problemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProblemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", timelimit='" + getTimelimit() + "'" +
            ", pdflocation='" + getPdflocation() + "'" +
            "}";
    }
}
