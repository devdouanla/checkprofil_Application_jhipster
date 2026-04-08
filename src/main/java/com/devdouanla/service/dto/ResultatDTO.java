package com.devdouanla.service.dto;

import com.devdouanla.domain.enumeration.Mention;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.Resultat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResultatDTO implements Serializable {

    private Long id;

    @NotNull
    private Float scoreTotal;

    @NotNull
    private Float scoreMax;

    @NotNull
    private Float pourcentage;

    @NotNull
    private Mention mention;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getScoreTotal() {
        return scoreTotal;
    }

    public void setScoreTotal(Float scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public Float getScoreMax() {
        return scoreMax;
    }

    public void setScoreMax(Float scoreMax) {
        this.scoreMax = scoreMax;
    }

    public Float getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Float pourcentage) {
        this.pourcentage = pourcentage;
    }

    public Mention getMention() {
        return mention;
    }

    public void setMention(Mention mention) {
        this.mention = mention;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResultatDTO)) {
            return false;
        }

        ResultatDTO resultatDTO = (ResultatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resultatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultatDTO{" +
            "id=" + getId() +
            ", scoreTotal=" + getScoreTotal() +
            ", scoreMax=" + getScoreMax() +
            ", pourcentage=" + getPourcentage() +
            ", mention='" + getMention() + "'" +
            "}";
    }
}
