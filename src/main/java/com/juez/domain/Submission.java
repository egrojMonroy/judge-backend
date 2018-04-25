package com.juez.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import com.juez.domain.enumeration.Veredict;

import com.juez.domain.enumeration.Language;

/**
 * A Submission.
 */
@Entity
@Table(name = "submission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Veredict status;

    @Column(name = "runtime")
    private Integer runtime;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @ManyToOne
    private Problem problem;

    @ManyToOne
    private User submitter;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Veredict getStatus() {
        return status;
    }

    public Submission status(Veredict status) {
        this.status = status;
        return this;
    }

    public void setStatus(Veredict status) {
        this.status = status;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public Submission runtime(Integer runtime) {
        this.runtime = runtime;
        return this;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Language getLanguage() {
        return language;
    }

    public Submission language(Language language) {
        this.language = language;
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Problem getProblem() {
        return problem;
    }

    public Submission problem(Problem problem) {
        this.problem = problem;
        return this;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public User getSubmitter() {
        return submitter;
    }

    public Submission submitter(User user) {
        this.submitter = user;
        return this;
    }

    public void setSubmitter(User user) {
        this.submitter = user;
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
        Submission submission = (Submission) o;
        if (submission.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), submission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Submission{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", runtime=" + getRuntime() +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
