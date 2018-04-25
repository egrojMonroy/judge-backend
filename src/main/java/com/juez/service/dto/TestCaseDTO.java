package com.juez.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TestCase entity.
 */
public class TestCaseDTO implements Serializable {

    private Long id;

    @Size(max = 3000)
    private String inputfl;

    @Size(max = 3000)
    private String outputfl;

    private Boolean show;

    private Long problemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInputfl() {
        return inputfl;
    }

    public void setInputfl(String inputfl) {
        this.inputfl = inputfl;
    }

    public String getOutputfl() {
        return outputfl;
    }

    public void setOutputfl(String outputfl) {
        this.outputfl = outputfl;
    }

    public Boolean isShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestCaseDTO testCaseDTO = (TestCaseDTO) o;
        if(testCaseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testCaseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestCaseDTO{" +
            "id=" + getId() +
            ", inputfl='" + getInputfl() + "'" +
            ", outputfl='" + getOutputfl() + "'" +
            ", show='" + isShow() + "'" +
            "}";
    }
}
