package com.devdouanla.service.dto;

import com.devdouanla.domain.enumeration.Difficulte;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.Question} entity.
 */
@Schema(description = "Question appartient au pool d'une Compétence.\nSa propre difficulte permet le filtrage lors du tirage.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionDTO implements Serializable {

    private Long id;

    @NotNull
    private String enonce;

    @NotNull
    private String reponseTexte;

    @NotNull
    private Integer points;

    private String explication;

    @NotNull
    private Difficulte difficulte;

    private CompetenceDTO competence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public String getReponseTexte() {
        return reponseTexte;
    }

    public void setReponseTexte(String reponseTexte) {
        this.reponseTexte = reponseTexte;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getExplication() {
        return explication;
    }

    public void setExplication(String explication) {
        this.explication = explication;
    }

    public Difficulte getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(Difficulte difficulte) {
        this.difficulte = difficulte;
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
        if (!(o instanceof QuestionDTO)) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + getId() +
            ", enonce='" + getEnonce() + "'" +
            ", reponseTexte='" + getReponseTexte() + "'" +
            ", points=" + getPoints() +
            ", explication='" + getExplication() + "'" +
            ", difficulte='" + getDifficulte() + "'" +
            ", competence=" + getCompetence() +
            "}";
    }
}
