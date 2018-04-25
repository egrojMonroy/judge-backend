package com.juez.domain;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Coder.
 */
@Entity
@Table(name = "coder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Coder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "ranking")
    private String ranking;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    /**
     * Registered coders in contest
     */
    @ApiModelProperty(value = "Registered coders in contest")
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "coder_contest",
               joinColumns = @JoinColumn(name="coders_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="contests_id", referencedColumnName="id"))
    private Set<Contest> contests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRanking() {
        return ranking;
    }

    public Coder ranking(String ranking) {
        this.ranking = ranking;
        return this;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public User getUser() {
        return user;
    }

    public Coder user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Contest> getContests() {
        return contests;
    }

    public Coder contests(Set<Contest> contests) {
        this.contests = contests;
        return this;
    }

    public Coder addContest(Contest contest) {
        this.contests.add(contest);
        contest.getCoders().add(this);
        return this;
    }

    public Coder removeContest(Contest contest) {
        this.contests.remove(contest);
        contest.getCoders().remove(this);
        return this;
    }

    public void setContests(Set<Contest> contests) {
        this.contests = contests;
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
        Coder coder = (Coder) o;
        if (coder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Coder{" +
            "id=" + getId() +
            ", ranking='" + getRanking() + "'" +
            "}";
    }
}
