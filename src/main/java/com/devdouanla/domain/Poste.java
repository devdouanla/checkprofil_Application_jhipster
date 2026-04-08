package com.devdouanla.domain;

import com.devdouanla.domain.enumeration.Niveau;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Poste.
 */
@Entity
@Table(name = "poste")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Poste implements Serializable {

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
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau", nullable = false)
    private Niveau niveau;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "poste")
    @JsonIgnoreProperties(value = { "evaluationses", "poste" }, allowSetters = true)
    private Set<Employe> employeses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "poste")
    @JsonIgnoreProperties(value = { "competence", "poste" }, allowSetters = true)
    private Set<CompetenceRequise> competencesRequiseses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "posteses" }, allowSetters = true)
    private Manager manager;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Poste id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Poste nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Niveau getNiveau() {
        return this.niveau;
    }

    public Poste niveau(Niveau niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Set<Employe> getEmployeses() {
        return this.employeses;
    }

    public void setEmployeses(Set<Employe> employes) {
        if (this.employeses != null) {
            this.employeses.forEach(i -> i.setPoste(null));
        }
        if (employes != null) {
            employes.forEach(i -> i.setPoste(this));
        }
        this.employeses = employes;
    }

    public Poste employeses(Set<Employe> employes) {
        this.setEmployeses(employes);
        return this;
    }

    public Poste addEmployes(Employe employe) {
        this.employeses.add(employe);
        employe.setPoste(this);
        return this;
    }

    public Poste removeEmployes(Employe employe) {
        this.employeses.remove(employe);
        employe.setPoste(null);
        return this;
    }

    public Set<CompetenceRequise> getCompetencesRequiseses() {
        return this.competencesRequiseses;
    }

    public void setCompetencesRequiseses(Set<CompetenceRequise> competenceRequises) {
        if (this.competencesRequiseses != null) {
            this.competencesRequiseses.forEach(i -> i.setPoste(null));
        }
        if (competenceRequises != null) {
            competenceRequises.forEach(i -> i.setPoste(this));
        }
        this.competencesRequiseses = competenceRequises;
    }

    public Poste competencesRequiseses(Set<CompetenceRequise> competenceRequises) {
        this.setCompetencesRequiseses(competenceRequises);
        return this;
    }

    public Poste addCompetencesRequises(CompetenceRequise competenceRequise) {
        this.competencesRequiseses.add(competenceRequise);
        competenceRequise.setPoste(this);
        return this;
    }

    public Poste removeCompetencesRequises(CompetenceRequise competenceRequise) {
        this.competencesRequiseses.remove(competenceRequise);
        competenceRequise.setPoste(null);
        return this;
    }

    public Manager getManager() {
        return this.manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Poste manager(Manager manager) {
        this.setManager(manager);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Poste)) {
            return false;
        }
        return getId() != null && getId().equals(((Poste) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Poste{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", niveau='" + getNiveau() + "'" +
            "}";
    }
}
