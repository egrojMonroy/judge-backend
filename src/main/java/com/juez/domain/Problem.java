package com.juez.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Problem.
 */
@Entity
@Table(name = "problem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "timelimit")
    private Integer timelimit;

    @Column(name = "pdflocation")
    private String pdflocation;

    @OneToOne
    @JoinColumn(unique = true)
    private Code solution;

    @OneToMany(mappedBy = "problem")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Submission> submissions = new HashSet<>();

    @OneToMany(mappedBy = "problem")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TestCase> tests = new HashSet<>();

    @ManyToMany(mappedBy = "problems")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contest> contests = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Problem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimelimit() {
        return timelimit;
    }

    public Problem timelimit(Integer timelimit) {
        this.timelimit = timelimit;
        return this;
    }

    public void setTimelimit(Integer timelimit) {
        this.timelimit = timelimit;
    }

    public String getPdflocation() {
        return pdflocation;
    }

    public Problem pdflocation(String pdflocation) {
        this.pdflocation = pdflocation;
        return this;
    }

    public void setPdflocation(String pdflocation) {
        this.pdflocation = pdflocation;
    }

    public Code getSolution() {
        return solution;
    }

    public Problem solution(Code code) {
        this.solution = code;
        return this;
    }

    public void setSolution(Code code) {
        this.solution = code;
    }

    public Set<Submission> getSubmissions() {
        return submissions;
    }

    public Problem submissions(Set<Submission> submissions) {
        this.submissions = submissions;
        return this;
    }

    public Problem addSubmission(Submission submission) {
        this.submissions.add(submission);
        submission.setProblem(this);
        return this;
    }

    public Problem removeSubmission(Submission submission) {
        this.submissions.remove(submission);
        submission.setProblem(null);
        return this;
    }

    public void setSubmissions(Set<Submission> submissions) {
        this.submissions = submissions;
    }

    public Set<TestCase> getTests() {
        return tests;
    }

    public Problem tests(Set<TestCase> testCases) {
        this.tests = testCases;
        return this;
    }

    public Problem addTest(TestCase testCase) {
        this.tests.add(testCase);
        testCase.setProblem(this);
        return this;
    }

    public Problem removeTest(TestCase testCase) {
        this.tests.remove(testCase);
        testCase.setProblem(null);
        return this;
    }

    public void setTests(Set<TestCase> testCases) {
        this.tests = testCases;
    }

    public Set<Contest> getContests() {
        return contests;
    }

    public Problem contests(Set<Contest> contests) {
        this.contests = contests;
        return this;
    }

    public Problem addContest(Contest contest) {
        this.contests.add(contest);
        contest.getProblems().add(this);
        return this;
    }

    public Problem removeContest(Contest contest) {
        this.contests.remove(contest);
        contest.getProblems().remove(this);
        return this;
    }

    public void setContests(Set<Contest> contests) {
        this.contests = contests;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Problem problem = (Problem) o;
        if (problem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), problem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Problem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", timelimit='" + getTimelimit() + "'" +
            ", pdflocation='" + getPdflocation() + "'" +
            "}";
    }
}
