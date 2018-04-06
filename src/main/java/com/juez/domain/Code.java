package com.juez.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Code.
 */
@Entity
@Table(name = "code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Code implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "dateupload")
    private ZonedDateTime dateupload;

    @OneToOne
    @JoinColumn(unique = true)
    private Submission submission;

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

    public Code name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDateupload() {
        return dateupload;
    }

    public Code dateupload(ZonedDateTime dateupload) {
        this.dateupload = dateupload;
        return this;
    }

    public void setDateupload(ZonedDateTime dateupload) {
        this.dateupload = dateupload;
    }

    public Submission getSubmission() {
        return submission;
    }

    public Code submission(Submission submission) {
        this.submission = submission;
        return this;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
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
        Code code = (Code) o;
        if (code.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), code.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Code{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateupload='" + getDateupload() + "'" +
            "}";
    }
}
