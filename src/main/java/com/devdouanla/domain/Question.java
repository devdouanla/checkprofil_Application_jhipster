package com.devdouanla.domain;

import com.devdouanla.domain.enumeration.Difficulte;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Question appartient au pool d'une Compétence.
 * Sa propre difficulte permet le filtrage lors du tirage.
 */
@Entity
@Table(name = "question")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "enonce", nullable = false)
    private String enonce;

    @NotNull
    @Column(name = "reponse_texte", nullable = false)
    private String reponseTexte;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "explication")
    private String explication;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulte", nullable = false)
    private Difficulte difficulte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "questionses", "expertses", "epreuveses" }, allowSetters = true)
    private Competence competence;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnonce() {
        return this.enonce;
    }

    public Question enonce(String enonce) {
        this.setEnonce(enonce);
        return this;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public String getReponseTexte() {
        return this.reponseTexte;
    }

    public Question reponseTexte(String reponseTexte) {
        this.setReponseTexte(reponseTexte);
        return this;
    }

    public void setReponseTexte(String reponseTexte) {
        this.reponseTexte = reponseTexte;
    }

    public Integer getPoints() {
        return this.points;
    }

    public Question points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getExplication() {
        return this.explication;
    }

    public Question explication(String explication) {
        this.setExplication(explication);
        return this;
    }

    public void setExplication(String explication) {
        this.explication = explication;
    }

    public Difficulte getDifficulte() {
        return this.difficulte;
    }

    public Question difficulte(Difficulte difficulte) {
        this.setDifficulte(difficulte);
        return this;
    }

    public void setDifficulte(Difficulte difficulte) {
        this.difficulte = difficulte;
    }

    public Competence getCompetence() {
        return this.competence;
    }

    public void setCompetence(Competence competence) {
        this.competence = competence;
    }

    public Question competence(Competence competence) {
        this.setCompetence(competence);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return getId() != null && getId().equals(((Question) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", enonce='" + getEnonce() + "'" +
            ", reponseTexte='" + getReponseTexte() + "'" +
            ", points=" + getPoints() +
            ", explication='" + getExplication() + "'" +
            ", difficulte='" + getDifficulte() + "'" +
            "}";
    }
}
