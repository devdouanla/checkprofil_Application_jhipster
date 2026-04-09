package com.devdouanla.domain;

import com.devdouanla.domain.enumeration.Mention;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * A Resultat.
 */
@Entity
@Table(name = "resultat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Resultat implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "score_total", nullable = false)
    private Float scoreTotal;

    @NotNull
    @Column(name = "score_max", nullable = false)
    private Float scoreMax;

    @NotNull
    @Column(name = "pourcentage", nullable = false)
    private Float pourcentage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mention", nullable = false)
    private Mention mention;

    @JsonIgnoreProperties(value = { "resultat", "questionsAsks", "reponseses", "evaluation", "epreuve" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "resultat")
    private SessionTest session;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Resultat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getScoreTotal() {
        return this.scoreTotal;
    }

    public Resultat scoreTotal(Float scoreTotal) {
        this.setScoreTotal(scoreTotal);
        return this;
    }

    public void setScoreTotal(Float scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public Float getScoreMax() {
        return this.scoreMax;
    }

    public Resultat scoreMax(Float scoreMax) {
        this.setScoreMax(scoreMax);
        return this;
    }

    public void setScoreMax(Float scoreMax) {
        this.scoreMax = scoreMax;
    }

    public Float getPourcentage() {
        return this.pourcentage;
    }

    public Resultat pourcentage(Float pourcentage) {
        this.setPourcentage(pourcentage);
        return this;
    }

    public void setPourcentage(Float pourcentage) {
        this.pourcentage = pourcentage;
    }

    public Mention getMention() {
        return this.mention;
    }

    public Resultat mention(Mention mention) {
        this.setMention(mention);
        return this;
    }

    public void setMention(Mention mention) {
        this.mention = mention;
    }

    public SessionTest getSession() {
        return this.session;
    }

    public void setSession(SessionTest sessionTest) {
        if (this.session != null) {
            this.session.setResultat(null);
        }
        if (sessionTest != null) {
            sessionTest.setResultat(this);
        }
        this.session = sessionTest;
    }

    public Resultat session(SessionTest sessionTest) {
        this.setSession(sessionTest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resultat)) {
            return false;
        }
        return getId() != null && getId().equals(((Resultat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resultat{" +
            "id=" + getId() +
            ", scoreTotal=" + getScoreTotal() +
            ", scoreMax=" + getScoreMax() +
            ", pourcentage=" + getPourcentage() +
            ", mention='" + getMention() + "'" +
            "}";
    }
}
