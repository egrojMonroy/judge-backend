package com.juez.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Contest entity.
 */
public class ContestDTO implements Serializable {

    private Long id;

    private String name;

    private ZonedDateTime startdate;

    private ZonedDateTime enddate;

    private String type;

    private Long creatorId;

    private Set<ProblemDTO> problems = new HashSet<>();

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

    public ZonedDateTime getStartdate() {
        return startdate;
    }

    public void setStartdate(ZonedDateTime startdate) {
        this.startdate = startdate;
    }

    public ZonedDateTime getEnddate() {
        return enddate;
    }

    public void setEnddate(ZonedDateTime enddate) {
        this.enddate = enddate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long userId) {
        this.creatorId = userId;
    }

    public Set<ProblemDTO> getProblems() {
        return problems;
    }

    public void setProblems(Set<ProblemDTO> problems) {
        this.problems = problems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContestDTO contestDTO = (ContestDTO) o;
        if(contestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContestDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startdate='" + getStartdate() + "'" +
            ", enddate='" + getEnddate() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
