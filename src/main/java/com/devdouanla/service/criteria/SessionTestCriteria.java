package com.devdouanla.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devdouanla.domain.SessionTest} entity. This class is used
 * in {@link com.devdouanla.web.rest.SessionTestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /session-tests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionTestCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter scoreObtenu;

    private InstantFilter dateDebut;

    private InstantFilter dateFin;

    private LongFilter resultatId;

    private LongFilter questionsAskId;

    private LongFilter reponsesId;

    private LongFilter evaluationId;

    private LongFilter epreuveId;

    private Boolean distinct;

    public SessionTestCriteria() {}

    public SessionTestCriteria(SessionTestCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.scoreObtenu = other.optionalScoreObtenu().map(FloatFilter::copy).orElse(null);
        this.dateDebut = other.optionalDateDebut().map(InstantFilter::copy).orElse(null);
        this.dateFin = other.optionalDateFin().map(InstantFilter::copy).orElse(null);
        this.resultatId = other.optionalResultatId().map(LongFilter::copy).orElse(null);
        this.questionsAskId = other.optionalQuestionsAskId().map(LongFilter::copy).orElse(null);
        this.reponsesId = other.optionalReponsesId().map(LongFilter::copy).orElse(null);
        this.evaluationId = other.optionalEvaluationId().map(LongFilter::copy).orElse(null);
        this.epreuveId = other.optionalEpreuveId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SessionTestCriteria copy() {
        return new SessionTestCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public FloatFilter getScoreObtenu() {
        return scoreObtenu;
    }

    public Optional<FloatFilter> optionalScoreObtenu() {
        return Optional.ofNullable(scoreObtenu);
    }

    public FloatFilter scoreObtenu() {
        if (scoreObtenu == null) {
            setScoreObtenu(new FloatFilter());
        }
        return scoreObtenu;
    }

    public void setScoreObtenu(FloatFilter scoreObtenu) {
        this.scoreObtenu = scoreObtenu;
    }

    public InstantFilter getDateDebut() {
        return dateDebut;
    }

    public Optional<InstantFilter> optionalDateDebut() {
        return Optional.ofNullable(dateDebut);
    }

    public InstantFilter dateDebut() {
        if (dateDebut == null) {
            setDateDebut(new InstantFilter());
        }
        return dateDebut;
    }

    public void setDateDebut(InstantFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public InstantFilter getDateFin() {
        return dateFin;
    }

    public Optional<InstantFilter> optionalDateFin() {
        return Optional.ofNullable(dateFin);
    }

    public InstantFilter dateFin() {
        if (dateFin == null) {
            setDateFin(new InstantFilter());
        }
        return dateFin;
    }

    public void setDateFin(InstantFilter dateFin) {
        this.dateFin = dateFin;
    }

    public LongFilter getResultatId() {
        return resultatId;
    }

    public Optional<LongFilter> optionalResultatId() {
        return Optional.ofNullable(resultatId);
    }

    public LongFilter resultatId() {
        if (resultatId == null) {
            setResultatId(new LongFilter());
        }
        return resultatId;
    }

    public void setResultatId(LongFilter resultatId) {
        this.resultatId = resultatId;
    }

    public LongFilter getQuestionsAskId() {
        return questionsAskId;
    }

    public Optional<LongFilter> optionalQuestionsAskId() {
        return Optional.ofNullable(questionsAskId);
    }

    public LongFilter questionsAskId() {
        if (questionsAskId == null) {
            setQuestionsAskId(new LongFilter());
        }
        return questionsAskId;
    }

    public void setQuestionsAskId(LongFilter questionsAskId) {
        this.questionsAskId = questionsAskId;
    }

    public LongFilter getReponsesId() {
        return reponsesId;
    }

    public Optional<LongFilter> optionalReponsesId() {
        return Optional.ofNullable(reponsesId);
    }

    public LongFilter reponsesId() {
        if (reponsesId == null) {
            setReponsesId(new LongFilter());
        }
        return reponsesId;
    }

    public void setReponsesId(LongFilter reponsesId) {
        this.reponsesId = reponsesId;
    }

    public LongFilter getEvaluationId() {
        return evaluationId;
    }

    public Optional<LongFilter> optionalEvaluationId() {
        return Optional.ofNullable(evaluationId);
    }

    public LongFilter evaluationId() {
        if (evaluationId == null) {
            setEvaluationId(new LongFilter());
        }
        return evaluationId;
    }

    public void setEvaluationId(LongFilter evaluationId) {
        this.evaluationId = evaluationId;
    }

    public LongFilter getEpreuveId() {
        return epreuveId;
    }

    public Optional<LongFilter> optionalEpreuveId() {
        return Optional.ofNullable(epreuveId);
    }

    public LongFilter epreuveId() {
        if (epreuveId == null) {
            setEpreuveId(new LongFilter());
        }
        return epreuveId;
    }

    public void setEpreuveId(LongFilter epreuveId) {
        this.epreuveId = epreuveId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SessionTestCriteria that = (SessionTestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(scoreObtenu, that.scoreObtenu) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(resultatId, that.resultatId) &&
            Objects.equals(questionsAskId, that.questionsAskId) &&
            Objects.equals(reponsesId, that.reponsesId) &&
            Objects.equals(evaluationId, that.evaluationId) &&
            Objects.equals(epreuveId, that.epreuveId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scoreObtenu, dateDebut, dateFin, resultatId, questionsAskId, reponsesId, evaluationId, epreuveId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionTestCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalScoreObtenu().map(f -> "scoreObtenu=" + f + ", ").orElse("") +
            optionalDateDebut().map(f -> "dateDebut=" + f + ", ").orElse("") +
            optionalDateFin().map(f -> "dateFin=" + f + ", ").orElse("") +
            optionalResultatId().map(f -> "resultatId=" + f + ", ").orElse("") +
            optionalQuestionsAskId().map(f -> "questionsAskId=" + f + ", ").orElse("") +
            optionalReponsesId().map(f -> "reponsesId=" + f + ", ").orElse("") +
            optionalEvaluationId().map(f -> "evaluationId=" + f + ", ").orElse("") +
            optionalEpreuveId().map(f -> "epreuveId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
