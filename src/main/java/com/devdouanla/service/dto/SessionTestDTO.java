package com.devdouanla.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.SessionTest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionTestDTO implements Serializable {

    private Long id;

    private Float scoreObtenu;

    @NotNull
    private Instant dateDebut;

    private Instant dateFin;

    private ResultatDTO resultat;

    private EvaluationDTO evaluation;

    private EpreuveDTO epreuve;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getScoreObtenu() {
        return scoreObtenu;
    }

    public void setScoreObtenu(Float scoreObtenu) {
        this.scoreObtenu = scoreObtenu;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public ResultatDTO getResultat() {
        return resultat;
    }

    public void setResultat(ResultatDTO resultat) {
        this.resultat = resultat;
    }

    public EvaluationDTO getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(EvaluationDTO evaluation) {
        this.evaluation = evaluation;
    }

    public EpreuveDTO getEpreuve() {
        return epreuve;
    }

    public void setEpreuve(EpreuveDTO epreuve) {
        this.epreuve = epreuve;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionTestDTO)) {
            return false;
        }

        SessionTestDTO sessionTestDTO = (SessionTestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sessionTestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionTestDTO{" +
            "id=" + getId() +
            ", scoreObtenu=" + getScoreObtenu() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", resultat=" + getResultat() +
            ", evaluation=" + getEvaluation() +
            ", epreuve=" + getEpreuve() +
            "}";
    }
}
