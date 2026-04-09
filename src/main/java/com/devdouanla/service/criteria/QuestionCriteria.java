package com.devdouanla.service.criteria;

import com.devdouanla.domain.enumeration.Difficulte;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devdouanla.domain.Question} entity. This class is used
 * in {@link com.devdouanla.web.rest.QuestionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Difficulte
     */
    public static class DifficulteFilter extends Filter<Difficulte> {

        public DifficulteFilter() {}

        public DifficulteFilter(DifficulteFilter filter) {
            super(filter);
        }

        @Override
        public DifficulteFilter copy() {
            return new DifficulteFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter enonce;

    private StringFilter reponseTexte;

    private IntegerFilter points;

    private StringFilter explication;

    private DifficulteFilter difficulte;

    private LongFilter competenceId;

    private Boolean distinct;

    public QuestionCriteria() {}

    public QuestionCriteria(QuestionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.enonce = other.optionalEnonce().map(StringFilter::copy).orElse(null);
        this.reponseTexte = other.optionalReponseTexte().map(StringFilter::copy).orElse(null);
        this.points = other.optionalPoints().map(IntegerFilter::copy).orElse(null);
        this.explication = other.optionalExplication().map(StringFilter::copy).orElse(null);
        this.difficulte = other.optionalDifficulte().map(DifficulteFilter::copy).orElse(null);
        this.competenceId = other.optionalCompetenceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QuestionCriteria copy() {
        return new QuestionCriteria(this);
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

    public StringFilter getEnonce() {
        return enonce;
    }

    public Optional<StringFilter> optionalEnonce() {
        return Optional.ofNullable(enonce);
    }

    public StringFilter enonce() {
        if (enonce == null) {
            setEnonce(new StringFilter());
        }
        return enonce;
    }

    public void setEnonce(StringFilter enonce) {
        this.enonce = enonce;
    }

    public StringFilter getReponseTexte() {
        return reponseTexte;
    }

    public Optional<StringFilter> optionalReponseTexte() {
        return Optional.ofNullable(reponseTexte);
    }

    public StringFilter reponseTexte() {
        if (reponseTexte == null) {
            setReponseTexte(new StringFilter());
        }
        return reponseTexte;
    }

    public void setReponseTexte(StringFilter reponseTexte) {
        this.reponseTexte = reponseTexte;
    }

    public IntegerFilter getPoints() {
        return points;
    }

    public Optional<IntegerFilter> optionalPoints() {
        return Optional.ofNullable(points);
    }

    public IntegerFilter points() {
        if (points == null) {
            setPoints(new IntegerFilter());
        }
        return points;
    }

    public void setPoints(IntegerFilter points) {
        this.points = points;
    }

    public StringFilter getExplication() {
        return explication;
    }

    public Optional<StringFilter> optionalExplication() {
        return Optional.ofNullable(explication);
    }

    public StringFilter explication() {
        if (explication == null) {
            setExplication(new StringFilter());
        }
        return explication;
    }

    public void setExplication(StringFilter explication) {
        this.explication = explication;
    }

    public DifficulteFilter getDifficulte() {
        return difficulte;
    }

    public Optional<DifficulteFilter> optionalDifficulte() {
        return Optional.ofNullable(difficulte);
    }

    public DifficulteFilter difficulte() {
        if (difficulte == null) {
            setDifficulte(new DifficulteFilter());
        }
        return difficulte;
    }

    public void setDifficulte(DifficulteFilter difficulte) {
        this.difficulte = difficulte;
    }

    public LongFilter getCompetenceId() {
        return competenceId;
    }

    public Optional<LongFilter> optionalCompetenceId() {
        return Optional.ofNullable(competenceId);
    }

    public LongFilter competenceId() {
        if (competenceId == null) {
            setCompetenceId(new LongFilter());
        }
        return competenceId;
    }

    public void setCompetenceId(LongFilter competenceId) {
        this.competenceId = competenceId;
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
        final QuestionCriteria that = (QuestionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(enonce, that.enonce) &&
            Objects.equals(reponseTexte, that.reponseTexte) &&
            Objects.equals(points, that.points) &&
            Objects.equals(explication, that.explication) &&
            Objects.equals(difficulte, that.difficulte) &&
            Objects.equals(competenceId, that.competenceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enonce, reponseTexte, points, explication, difficulte, competenceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEnonce().map(f -> "enonce=" + f + ", ").orElse("") +
            optionalReponseTexte().map(f -> "reponseTexte=" + f + ", ").orElse("") +
            optionalPoints().map(f -> "points=" + f + ", ").orElse("") +
            optionalExplication().map(f -> "explication=" + f + ", ").orElse("") +
            optionalDifficulte().map(f -> "difficulte=" + f + ", ").orElse("") +
            optionalCompetenceId().map(f -> "competenceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
