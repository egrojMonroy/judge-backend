package com.juez.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Contest.
 */
@Entity
@Table(name = "contest")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "startdate")
    private ZonedDateTime startdate;

    @Column(name = "enddate")
    private ZonedDateTime enddate;

    @Column(name = "starttime")
    private ZonedDateTime starttime;

    @Column(name = "endtime")
    private ZonedDateTime endtime;

    @Column(name = "jhi_type")
    private String type;

    /**
     * list of problems in contest
     */
    @ApiModelProperty(value = "list of problems in contest")
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "contest_problem",
               joinColumns = @JoinColumn(name="contests_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="problems_id", referencedColumnName="id"))
    private Set<Problem> problems = new HashSet<>();

    @ManyToMany(mappedBy = "contests")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Coder> coders = new HashSet<>();

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

    public Contest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getStartdate() {
        return startdate;
    }

    public Contest startdate(ZonedDateTime startdate) {
        this.startdate = startdate;
        return this;
    }

    public void setStartdate(ZonedDateTime startdate) {
        this.startdate = startdate;
    }

    public ZonedDateTime getEnddate() {
        return enddate;
    }

    public Contest enddate(ZonedDateTime enddate) {
        this.enddate = enddate;
        return this;
    }

    public void setEnddate(ZonedDateTime enddate) {
        this.enddate = enddate;
    }

    public ZonedDateTime getStarttime() {
        return starttime;
    }

    public Contest starttime(ZonedDateTime starttime) {
        this.starttime = starttime;
        return this;
    }

    public void setStarttime(ZonedDateTime starttime) {
        this.starttime = starttime;
    }

    public ZonedDateTime getEndtime() {
        return endtime;
    }

    public Contest endtime(ZonedDateTime endtime) {
        this.endtime = endtime;
        return this;
    }

    public void setEndtime(ZonedDateTime endtime) {
        this.endtime = endtime;
    }

    public String getType() {
        return type;
    }

    public Contest type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Problem> getProblems() {
        return problems;
    }

    public Contest problems(Set<Problem> problems) {
        this.problems = problems;
        return this;
    }

    public Contest addProblem(Problem problem) {
        this.problems.add(problem);
        problem.getContests().add(this);
        return this;
    }

    public Contest removeProblem(Problem problem) {
        this.problems.remove(problem);
        problem.getContests().remove(this);
        return this;
    }

    public void setProblems(Set<Problem> problems) {
        this.problems = problems;
    }

    public Set<Coder> getCoders() {
        return coders;
    }

    public Contest coders(Set<Coder> coders) {
        this.coders = coders;
        return this;
    }

    public Contest addCoder(Coder coder) {
        this.coders.add(coder);
        coder.getContests().add(this);
        return this;
    }

    public Contest removeCoder(Coder coder) {
        this.coders.remove(coder);
        coder.getContests().remove(this);
        return this;
    }

    public void setCoders(Set<Coder> coders) {
        this.coders = coders;
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
        Contest contest = (Contest) o;
        if (contest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Contest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startdate='" + getStartdate() + "'" +
            ", enddate='" + getEnddate() + "'" +
            ", starttime='" + getStarttime() + "'" +
            ", endtime='" + getEndtime() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
