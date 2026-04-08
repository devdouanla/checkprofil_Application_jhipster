package com.devdouanla.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devdouanla.domain.Employe} entity. This class is used
 * in {@link com.devdouanla.web.rest.EmployeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private InstantFilter dateRecrutement;

    private LongFilter evaluationsId;

    private LongFilter posteId;

    private Boolean distinct;

    public EmployeCriteria() {}

    public EmployeCriteria(EmployeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.dateRecrutement = other.optionalDateRecrutement().map(InstantFilter::copy).orElse(null);
        this.evaluationsId = other.optionalEvaluationsId().map(LongFilter::copy).orElse(null);
        this.posteId = other.optionalPosteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EmployeCriteria copy() {
        return new EmployeCriteria(this);
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

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public InstantFilter getDateRecrutement() {
        return dateRecrutement;
    }

    public Optional<InstantFilter> optionalDateRecrutement() {
        return Optional.ofNullable(dateRecrutement);
    }

    public InstantFilter dateRecrutement() {
        if (dateRecrutement == null) {
            setDateRecrutement(new InstantFilter());
        }
        return dateRecrutement;
    }

    public void setDateRecrutement(InstantFilter dateRecrutement) {
        this.dateRecrutement = dateRecrutement;
    }

    public LongFilter getEvaluationsId() {
        return evaluationsId;
    }

    public Optional<LongFilter> optionalEvaluationsId() {
        return Optional.ofNullable(evaluationsId);
    }

    public LongFilter evaluationsId() {
        if (evaluationsId == null) {
            setEvaluationsId(new LongFilter());
        }
        return evaluationsId;
    }

    public void setEvaluationsId(LongFilter evaluationsId) {
        this.evaluationsId = evaluationsId;
    }

    public LongFilter getPosteId() {
        return posteId;
    }

    public Optional<LongFilter> optionalPosteId() {
        return Optional.ofNullable(posteId);
    }

    public LongFilter posteId() {
        if (posteId == null) {
            setPosteId(new LongFilter());
        }
        return posteId;
    }

    public void setPosteId(LongFilter posteId) {
        this.posteId = posteId;
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
        final EmployeCriteria that = (EmployeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(dateRecrutement, that.dateRecrutement) &&
            Objects.equals(evaluationsId, that.evaluationsId) &&
            Objects.equals(posteId, that.posteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, dateRecrutement, evaluationsId, posteId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalDateRecrutement().map(f -> "dateRecrutement=" + f + ", ").orElse("") +
            optionalEvaluationsId().map(f -> "evaluationsId=" + f + ", ").orElse("") +
            optionalPosteId().map(f -> "posteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
