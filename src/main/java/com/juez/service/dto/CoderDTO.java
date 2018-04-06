package com.juez.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Coder entity.
 */
public class CoderDTO implements Serializable {

    private Long id;

    private String ranking;

    private Set<ContestDTO> contests = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public Set<ContestDTO> getContests() {
        return contests;
    }

    public void setContests(Set<ContestDTO> contests) {
        this.contests = contests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CoderDTO coderDTO = (CoderDTO) o;
        if(coderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CoderDTO{" +
            "id=" + getId() +
            ", ranking='" + getRanking() + "'" +
            "}";
    }
}
