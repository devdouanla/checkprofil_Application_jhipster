package com.devdouanla.service.dto;

import com.devdouanla.domain.enumeration.Difficulte;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.Epreuve} entity.
 */
@Schema(
    description = "Une Epreuve définit les CRITÈRES de tirage aléatoire :\n- competence  → le domaine du pool de questions\n- difficulte  → le niveau de difficulté à piocher\n- nbQuestions → combien de questions tirer aléatoirement\nElle ne possède PLUS de collection de questions fixées."
)
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
    private Integer nbQuestions;

    @NotNull
    private Boolean genereParIA;

    @NotNull
    private Boolean publie;

    @NotNull
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

    public Integer getNbQuestions() {
        return nbQuestions;
    }

    public void setNbQuestions(Integer nbQuestions) {
        this.nbQuestions = nbQuestions;
    }

    public Boolean getGenereParIA() {
        return genereParIA;
    }

    public void setGenereParIA(Boolean genereParIA) {
        this.genereParIA = genereParIA;
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
            ", nbQuestions=" + getNbQuestions() +
            ", genereParIA='" + getGenereParIA() + "'" +
            ", publie='" + getPublie() + "'" +
            ", competence=" + getCompetence() +
            "}";
    }
}
