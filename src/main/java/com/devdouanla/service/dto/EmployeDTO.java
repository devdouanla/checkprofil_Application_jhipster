package com.devdouanla.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.Employe} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private Instant dateRecrutement;

    private PosteDTO poste;

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

    public Instant getDateRecrutement() {
        return dateRecrutement;
    }

    public void setDateRecrutement(Instant dateRecrutement) {
        this.dateRecrutement = dateRecrutement;
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
        if (!(o instanceof EmployeDTO)) {
            return false;
        }

        EmployeDTO employeDTO = (EmployeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", dateRecrutement='" + getDateRecrutement() + "'" +
            ", poste=" + getPoste() +
            "}";
    }
}
