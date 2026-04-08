package com.devdouanla.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.CompetenceRequise} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompetenceRequiseDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean obligatoire;

    private CompetenceDTO competence;

    private PosteDTO poste;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getObligatoire() {
        return obligatoire;
    }

    public void setObligatoire(Boolean obligatoire) {
        this.obligatoire = obligatoire;
    }

    public CompetenceDTO getCompetence() {
        return competence;
    }

    public void setCompetence(CompetenceDTO competence) {
        this.competence = competence;
    }

    public PosteDTO getPoste() {
        return poste;
    }

    public void setPoste(PosteDTO poste) {
        this.poste = poste;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompetenceRequiseDTO)) {
            return false;
        }

        CompetenceRequiseDTO competenceRequiseDTO = (CompetenceRequiseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, competenceRequiseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompetenceRequiseDTO{" +
            "id=" + getId() +
            ", obligatoire='" + getObligatoire() + "'" +
            ", competence=" + getCompetence() +
            ", poste=" + getPoste() +
            "}";
    }
}
