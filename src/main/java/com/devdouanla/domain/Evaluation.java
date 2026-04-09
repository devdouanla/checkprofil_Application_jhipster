package com.devdouanla.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Evaluation.
 */
@Entity
@Table(name = "evaluation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Evaluation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @Column(name = "date_fin")
    private Instant dateFin;

    @Column(name = "conforme")
    private Boolean conforme;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evaluation")
    @JsonIgnoreProperties(value = { "resultat", "questionsAsks", "reponseses", "evaluation", "epreuve" }, allowSetters = true)
    private Set<SessionTest> sessionses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "evaluationses", "poste" }, allowSetters = true)
    private Employe employe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evaluation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public Evaluation dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return this.dateFin;
    }

    public Evaluation dateFin(Instant dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getConforme() {
        return this.conforme;
    }

    public Evaluation conforme(Boolean conforme) {
        this.setConforme(conforme);
        return this;
    }

    public void setConforme(Boolean conforme) {
        this.conforme = conforme;
    }

    public Set<SessionTest> getSessionses() {
        return this.sessionses;
    }

    public void setSessionses(Set<SessionTest> sessionTests) {
        if (this.sessionses != null) {
            this.sessionses.forEach(i -> i.setEvaluation(null));
        }
        if (sessionTests != null) {
            sessionTests.forEach(i -> i.setEvaluation(this));
        }
        this.sessionses = sessionTests;
    }

    public Evaluation sessionses(Set<SessionTest> sessionTests) {
        this.setSessionses(sessionTests);
        return this;
    }

    public Evaluation addSessions(SessionTest sessionTest) {
        this.sessionses.add(sessionTest);
        sessionTest.setEvaluation(this);
        return this;
    }

    public Evaluation removeSessions(SessionTest sessionTest) {
        this.sessionses.remove(sessionTest);
        sessionTest.setEvaluation(null);
        return this;
    }

    public Employe getEmploye() {
        return this.employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Evaluation employe(Employe employe) {
        this.setEmploye(employe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evaluation)) {
            return false;
        }
        return getId() != null && getId().equals(((Evaluation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evaluation{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", conforme='" + getConforme() + "'" +
            "}";
    }
}
