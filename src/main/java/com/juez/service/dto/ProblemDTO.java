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

    private Boolean active;

    private Integer timelimit;

    private Integer timelimitjava;

    private Integer level;

    private Long solutionId;

    private Long creatorId;

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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(Integer timelimit) {
        this.timelimit = timelimit;
    }

    public Integer getTimelimitjava() {
        return timelimitjava;
    }

    public void setTimelimitjava(Integer timelimitjava) {
        this.timelimitjava = timelimitjava;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(Long codeId) {
        this.solutionId = codeId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long userId) {
        this.creatorId = userId;
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
            ", active='" + isActive() + "'" +
            ", timelimit=" + getTimelimit() +
            ", timelimitjava=" + getTimelimitjava() +
            ", level=" + getLevel() +
            "}";
    }
}
