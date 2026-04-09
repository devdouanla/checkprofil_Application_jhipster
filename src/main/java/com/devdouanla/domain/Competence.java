package com.devdouanla.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Competence.
 */
@Entity
@Table(name = "competence")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Competence implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "competence")
    @JsonIgnoreProperties(value = { "competence" }, allowSetters = true)
    private Set<Question> questionses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_competence__experts",
        joinColumns = @JoinColumn(name = "competence_id"),
        inverseJoinColumns = @JoinColumn(name = "experts_id")
    )
    @JsonIgnoreProperties(value = { "user", "competenceses" }, allowSetters = true)
    private Set<Expert> expertses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "competence")
    @JsonIgnoreProperties(value = { "sessionses", "competence" }, allowSetters = true)
    private Set<Epreuve> epreuveses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Competence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Competence nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Question> getQuestionses() {
        return this.questionses;
    }

    public void setQuestionses(Set<Question> questions) {
        if (this.questionses != null) {
            this.questionses.forEach(i -> i.setCompetence(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setCompetence(this));
        }
        this.questionses = questions;
    }

    public Competence questionses(Set<Question> questions) {
        this.setQuestionses(questions);
        return this;
    }

    public Competence addQuestions(Question question) {
        this.questionses.add(question);
        question.setCompetence(this);
        return this;
    }

    public Competence removeQuestions(Question question) {
        this.questionses.remove(question);
        question.setCompetence(null);
        return this;
    }

    public Set<Expert> getExpertses() {
        return this.expertses;
    }

    public void setExpertses(Set<Expert> experts) {
        this.expertses = experts;
    }

    public Competence expertses(Set<Expert> experts) {
        this.setExpertses(experts);
        return this;
    }

    public Competence addExperts(Expert expert) {
        this.expertses.add(expert);
        return this;
    }

    public Competence removeExperts(Expert expert) {
        this.expertses.remove(expert);
        return this;
    }

    public Set<Epreuve> getEpreuveses() {
        return this.epreuveses;
    }

    public void setEpreuveses(Set<Epreuve> epreuves) {
        if (this.epreuveses != null) {
            this.epreuveses.forEach(i -> i.setCompetence(null));
        }
        if (epreuves != null) {
            epreuves.forEach(i -> i.setCompetence(this));
        }
        this.epreuveses = epreuves;
    }

    public Competence epreuveses(Set<Epreuve> epreuves) {
        this.setEpreuveses(epreuves);
        return this;
    }

    public Competence addEpreuves(Epreuve epreuve) {
        this.epreuveses.add(epreuve);
        epreuve.setCompetence(this);
        return this;
    }

    public Competence removeEpreuves(Epreuve epreuve) {
        this.epreuveses.remove(epreuve);
        epreuve.setCompetence(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Competence)) {
            return false;
        }
        return getId() != null && getId().equals(((Competence) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Competence{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
