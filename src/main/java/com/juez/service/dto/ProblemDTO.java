package com.juez.service.dto;


import javax.validation.constraints.*;
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

    private Integer level;

    @Size(max = 3000)
    private String definition;

    @Size(max = 2000)
    private String inputDef;

    @Size(max = 2000)
    private String outputDef;

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

    public Integer getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(Integer timelimit) {
        this.timelimit = timelimit;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getInputDef() {
        return inputDef;
    }

    public void setInputDef(String inputDef) {
        this.inputDef = inputDef;
    }

    public String getOutputDef() {
        return outputDef;
    }

    public void setOutputDef(String outputDef) {
        this.outputDef = outputDef;
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
            ", timelimit=" + getTimelimit() +
            ", level=" + getLevel() +
            ", definition='" + getDefinition() + "'" +
            ", inputDef='" + getInputDef() + "'" +
            ", outputDef='" + getOutputDef() + "'" +
            "}";
    }
}
