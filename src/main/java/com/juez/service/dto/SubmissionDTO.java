package com.juez.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.juez.domain.enumeration.Veredict;
import com.juez.domain.enumeration.Language;

/**
 * A DTO for the Submission entity.
 */
public class SubmissionDTO implements Serializable {

    private Long id;

    private Veredict status;

    private Integer runtime;

    private Language language;

    private ZonedDateTime dateupload;

    private Long submitterId;

    private Long problemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Veredict getStatus() {
        return status;
    }

    public void setStatus(Veredict status) {
        this.status = status;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ZonedDateTime getDateupload() {
        return dateupload;
    }

    public void setDateupload(ZonedDateTime dateupload) {
        this.dateupload = dateupload;
    }

    public Long getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(Long userId) {
        this.submitterId = userId;
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

        SubmissionDTO submissionDTO = (SubmissionDTO) o;
        if(submissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), submissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubmissionDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", runtime=" + getRuntime() +
            ", language='" + getLanguage() + "'" +
            ", dateupload='" + getDateupload() + "'" +
            "}";
    }
}
