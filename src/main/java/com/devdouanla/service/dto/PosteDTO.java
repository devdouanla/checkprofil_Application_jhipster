package com.devdouanla.service.dto;

import com.devdouanla.domain.enumeration.Niveau;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.Poste} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PosteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private Niveau niveau;

    private ManagerDTO manager;

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

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public ManagerDTO getManager() {
        return manager;
    }

    public void setManager(ManagerDTO manager) {
        this.manager = manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PosteDTO)) {
            return false;
        }

        PosteDTO posteDTO = (PosteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, posteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PosteDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", niveau='" + getNiveau() + "'" +
            ", manager=" + getManager() +
            "}";
    }
}
