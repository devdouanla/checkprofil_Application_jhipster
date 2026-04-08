package com.devdouanla.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.devdouanla.domain.Competence} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompetenceDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private Set<ExpertDTO> expertses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<ExpertDTO> getExpertses() {
        return expertses;
    }

    public void setExpertses(Set<ExpertDTO> expertses) {
        this.expertses = expertses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompetenceDTO)) {
            return false;
        }

        CompetenceDTO competenceDTO = (CompetenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, competenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompetenceDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", expertses=" + getExpertses() +
            "}";
    }
}
