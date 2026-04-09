package com.devdouanla.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * QuestionAsk = une question tirée aléatoirement pour une SessionTest.
 * C'est la matérialisation du tirage : SessionTest → QuestionAsk → Question.
 */
@Entity
@Table(name = "question_ask")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionAsk implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ordre")
    private Integer ordre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "competence" }, allowSetters = true)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "resultat", "questionsAsks", "reponseses", "evaluation", "epreuve" }, allowSetters = true)
    private SessionTest session;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuestionAsk id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrdre() {
        return this.ordre;
    }

    public QuestionAsk ordre(Integer ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public QuestionAsk question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public SessionTest getSession() {
        return this.session;
    }

    public void setSession(SessionTest sessionTest) {
        this.session = sessionTest;
    }

    public QuestionAsk session(SessionTest sessionTest) {
        this.setSession(sessionTest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionAsk)) {
            return false;
        }
        return getId() != null && getId().equals(((QuestionAsk) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionAsk{" +
            "id=" + getId() +
            ", ordre=" + getOrdre() +
            "}";
    }
}
