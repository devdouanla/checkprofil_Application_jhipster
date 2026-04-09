package com.devdouanla.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * A CompetenceRequise.
 */
@Entity
@Table(name = "competence_requise")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompetenceRequise implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "obligatoire", nullable = false)
    private Boolean obligatoire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "questionses", "expertses", "epreuveses" }, allowSetters = true)
    private Competence competence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "employeses", "competencesRequiseses", "manager" }, allowSetters = true)
    private Poste poste;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompetenceRequise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getObligatoire() {
        return this.obligatoire;
    }

    public CompetenceRequise obligatoire(Boolean obligatoire) {
        this.setObligatoire(obligatoire);
        return this;
    }

    public void setObligatoire(Boolean obligatoire) {
        this.obligatoire = obligatoire;
    }

    public Competence getCompetence() {
        return this.competence;
    }

    public void setCompetence(Competence competence) {
        this.competence = competence;
    }

    public CompetenceRequise competence(Competence competence) {
        this.setCompetence(competence);
        return this;
    }

    public Poste getPoste() {
        return this.poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public CompetenceRequise poste(Poste poste) {
        this.setPoste(poste);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompetenceRequise)) {
            return false;
        }
        return getId() != null && getId().equals(((CompetenceRequise) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompetenceRequise{" +
            "id=" + getId() +
            ", obligatoire='" + getObligatoire() + "'" +
            "}";
    }
}
