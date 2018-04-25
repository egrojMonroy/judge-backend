package com.juez.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A TestCase.
 */
@Entity
@Table(name = "test_case")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 3000)
    @Column(name = "inputfl", length = 3000)
    private String inputfl;

    @Size(max = 3000)
    @Column(name = "outputfl", length = 3000)
    private String outputfl;

    @Column(name = "jhi_show")
    private Boolean show;

    @ManyToOne
    private Problem problem;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInputfl() {
        return inputfl;
    }

    public TestCase inputfl(String inputfl) {
        this.inputfl = inputfl;
        return this;
    }

    public void setInputfl(String inputfl) {
        this.inputfl = inputfl;
    }

    public String getOutputfl() {
        return outputfl;
    }

    public TestCase outputfl(String outputfl) {
        this.outputfl = outputfl;
        return this;
    }

    public void setOutputfl(String outputfl) {
        this.outputfl = outputfl;
    }

    public Boolean isShow() {
        return show;
    }

    public TestCase show(Boolean show) {
        this.show = show;
        return this;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Problem getProblem() {
        return problem;
    }

    public TestCase problem(Problem problem) {
        this.problem = problem;
        return this;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestCase testCase = (TestCase) o;
        if (testCase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testCase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestCase{" +
            "id=" + getId() +
            ", inputfl='" + getInputfl() + "'" +
            ", outputfl='" + getOutputfl() + "'" +
            ", show='" + isShow() + "'" +
            "}";
    }
}
