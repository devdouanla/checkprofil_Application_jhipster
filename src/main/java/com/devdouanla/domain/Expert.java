package com.devdouanla.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Expert.
 */
@Entity
@Table(name = "expert")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Expert implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "expertses")
    @JsonIgnoreProperties(value = { "epreuveses", "expertses" }, allowSetters = true)
    private Set<Competence> competenceses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Expert id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Expert user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Competence> getCompetenceses() {
        return this.competenceses;
    }

    public void setCompetenceses(Set<Competence> competences) {
        if (this.competenceses != null) {
            this.competenceses.forEach(i -> i.removeExperts(this));
        }
        if (competences != null) {
            competences.forEach(i -> i.addExperts(this));
        }
        this.competenceses = competences;
    }

    public Expert competenceses(Set<Competence> competences) {
        this.setCompetenceses(competences);
        return this;
    }

    public Expert addCompetences(Competence competence) {
        this.competenceses.add(competence);
        competence.getExpertses().add(this);
        return this;
    }

    public Expert removeCompetences(Competence competence) {
        this.competenceses.remove(competence);
        competence.getExpertses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expert)) {
            return false;
        }
        return getId() != null && getId().equals(((Expert) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Expert{" +
            "id=" + getId() +
            "}";
    }
}
