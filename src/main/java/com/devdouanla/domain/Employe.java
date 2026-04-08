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
 * A Employe.
 */
@Entity
@Table(name = "employe")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employe implements Serializable {

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

    @NotNull
    @Column(name = "date_recrutement", nullable = false)
    private Instant dateRecrutement;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employe")
    @JsonIgnoreProperties(value = { "sessionses", "employe" }, allowSetters = true)
    private Set<Evaluation> evaluationses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "employeses", "competencesRequiseses", "manager" }, allowSetters = true)
    private Poste poste;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Employe nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Instant getDateRecrutement() {
        return this.dateRecrutement;
    }

    public Employe dateRecrutement(Instant dateRecrutement) {
        this.setDateRecrutement(dateRecrutement);
        return this;
    }

    public void setDateRecrutement(Instant dateRecrutement) {
        this.dateRecrutement = dateRecrutement;
    }

    public Set<Evaluation> getEvaluationses() {
        return this.evaluationses;
    }

    public void setEvaluationses(Set<Evaluation> evaluations) {
        if (this.evaluationses != null) {
            this.evaluationses.forEach(i -> i.setEmploye(null));
        }
        if (evaluations != null) {
            evaluations.forEach(i -> i.setEmploye(this));
        }
        this.evaluationses = evaluations;
    }

    public Employe evaluationses(Set<Evaluation> evaluations) {
        this.setEvaluationses(evaluations);
        return this;
    }

    public Employe addEvaluations(Evaluation evaluation) {
        this.evaluationses.add(evaluation);
        evaluation.setEmploye(this);
        return this;
    }

    public Employe removeEvaluations(Evaluation evaluation) {
        this.evaluationses.remove(evaluation);
        evaluation.setEmploye(null);
        return this;
    }

    public Poste getPoste() {
        return this.poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public Employe poste(Poste poste) {
        this.setPoste(poste);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employe)) {
            return false;
        }
        return getId() != null && getId().equals(((Employe) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employe{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", dateRecrutement='" + getDateRecrutement() + "'" +
            "}";
    }
}
