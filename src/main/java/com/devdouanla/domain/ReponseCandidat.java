package com.devdouanla.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ReponseCandidat.
 */
@Entity
@Table(name = "reponse_candidat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReponseCandidat implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "est_correcte", nullable = false)
    private Boolean estCorrecte;

    @NotNull
    @Column(name = "date_reponse", nullable = false)
    private Instant dateReponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "epreuve" }, allowSetters = true)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "resultat", "reponseses", "evaluation", "epreuves" }, allowSetters = true)
    private SessionTest session;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReponseCandidat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstCorrecte() {
        return this.estCorrecte;
    }

    public ReponseCandidat estCorrecte(Boolean estCorrecte) {
        this.setEstCorrecte(estCorrecte);
        return this;
    }

    public void setEstCorrecte(Boolean estCorrecte) {
        this.estCorrecte = estCorrecte;
    }

    public Instant getDateReponse() {
        return this.dateReponse;
    }

    public ReponseCandidat dateReponse(Instant dateReponse) {
        this.setDateReponse(dateReponse);
        return this;
    }

    public void setDateReponse(Instant dateReponse) {
        this.dateReponse = dateReponse;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ReponseCandidat question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public SessionTest getSession() {
        return this.session;
    }

    public void setSession(SessionTest sessionTest) {
        this.session = sessionTest;
    }

    public ReponseCandidat session(SessionTest sessionTest) {
        this.setSession(sessionTest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReponseCandidat)) {
            return false;
        }
        return getId() != null && getId().equals(((ReponseCandidat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReponseCandidat{" +
            "id=" + getId() +
            ", estCorrecte='" + getEstCorrecte() + "'" +
            ", dateReponse='" + getDateReponse() + "'" +
            "}";
    }
}
