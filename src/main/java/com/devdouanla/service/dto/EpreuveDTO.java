package com.devdouanla.service.dto;

import com.devdouanla.domain.enumeration.Difficulte;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.Epreuve} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EpreuveDTO implements Serializable {

    private Long id;

    @NotNull
    private String titre;

    @NotNull
    private String enonce;

    @NotNull
    private Difficulte difficulte;

    @NotNull
    private Integer duree;

    @NotNull
    private Boolean genereParIA;

    private Integer nbInt;

    @NotNull
    private Boolean publie;

    private CompetenceDTO competence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public Difficulte getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(Difficulte difficulte) {
        this.difficulte = difficulte;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public Boolean getGenereParIA() {
        return genereParIA;
    }

    public void setGenereParIA(Boolean genereParIA) {
        this.genereParIA = genereParIA;
    }

    public Integer getNbInt() {
        return nbInt;
    }

    public void setNbInt(Integer nbInt) {
        this.nbInt = nbInt;
    }

    public Boolean getPublie() {
        return publie;
    }

    public void setPublie(Boolean publie) {
        this.publie = publie;
    }

    public CompetenceDTO getCompetence() {
        return competence;
    }

    public void setCompetence(CompetenceDTO competence) {
        this.competence = competence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EpreuveDTO)) {
            return false;
        }

        EpreuveDTO epreuveDTO = (EpreuveDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, epreuveDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EpreuveDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", enonce='" + getEnonce() + "'" +
            ", difficulte='" + getDifficulte() + "'" +
            ", duree=" + getDuree() +
            ", genereParIA='" + getGenereParIA() + "'" +
            ", nbInt=" + getNbInt() +
            ", publie='" + getPublie() + "'" +
            ", competence=" + getCompetence() +
            "}";
    }
}
