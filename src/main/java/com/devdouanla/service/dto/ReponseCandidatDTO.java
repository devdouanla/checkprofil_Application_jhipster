package com.devdouanla.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.ReponseCandidat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReponseCandidatDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean estCorrecte;

    @NotNull
    private Instant dateReponse;

    private QuestionDTO question;

    private SessionTestDTO session;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstCorrecte() {
        return estCorrecte;
    }

    public void setEstCorrecte(Boolean estCorrecte) {
        this.estCorrecte = estCorrecte;
    }

    public Instant getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(Instant dateReponse) {
        this.dateReponse = dateReponse;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public SessionTestDTO getSession() {
        return session;
    }

    public void setSession(SessionTestDTO session) {
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReponseCandidatDTO)) {
            return false;
        }

        ReponseCandidatDTO reponseCandidatDTO = (ReponseCandidatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reponseCandidatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReponseCandidatDTO{" +
            "id=" + getId() +
            ", estCorrecte='" + getEstCorrecte() + "'" +
            ", dateReponse='" + getDateReponse() + "'" +
            ", question=" + getQuestion() +
            ", session=" + getSession() +
            "}";
    }
}
