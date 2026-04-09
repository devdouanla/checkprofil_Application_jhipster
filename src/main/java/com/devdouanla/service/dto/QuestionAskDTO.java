package com.devdouanla.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.QuestionAsk} entity.
 */
@Schema(
    description = "QuestionAsk = une question tirée aléatoirement pour une SessionTest.\nC'est la matérialisation du tirage : SessionTest → QuestionAsk → Question."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionAskDTO implements Serializable {

    private Long id;

    private Integer ordre;

    private QuestionDTO question;

    private SessionTestDTO session;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
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
        if (!(o instanceof QuestionAskDTO)) {
            return false;
        }

        QuestionAskDTO questionAskDTO = (QuestionAskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionAskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionAskDTO{" +
            "id=" + getId() +
            ", ordre=" + getOrdre() +
            ", question=" + getQuestion() +
            ", session=" + getSession() +
            "}";
    }
}
