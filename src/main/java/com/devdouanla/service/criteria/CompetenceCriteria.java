package com.devdouanla.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devdouanla.domain.Competence} entity. This class is used
 * in {@link com.devdouanla.web.rest.CompetenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /competences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompetenceCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private LongFilter epreuvesId;

    private LongFilter expertsId;

    private Boolean distinct;

    public CompetenceCriteria() {}

    public CompetenceCriteria(CompetenceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.epreuvesId = other.optionalEpreuvesId().map(LongFilter::copy).orElse(null);
        this.expertsId = other.optionalExpertsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CompetenceCriteria copy() {
        return new CompetenceCriteria(this);
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

    public LongFilter getEpreuvesId() {
        return epreuvesId;
    }

    public Optional<LongFilter> optionalEpreuvesId() {
        return Optional.ofNullable(epreuvesId);
    }

    public LongFilter epreuvesId() {
        if (epreuvesId == null) {
            setEpreuvesId(new LongFilter());
        }
        return epreuvesId;
    }

    public void setEpreuvesId(LongFilter epreuvesId) {
        this.epreuvesId = epreuvesId;
    }

    public LongFilter getExpertsId() {
        return expertsId;
    }

    public Optional<LongFilter> optionalExpertsId() {
        return Optional.ofNullable(expertsId);
    }

    public LongFilter expertsId() {
        if (expertsId == null) {
            setExpertsId(new LongFilter());
        }
        return expertsId;
    }

    public void setExpertsId(LongFilter expertsId) {
        this.expertsId = expertsId;
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
        final CompetenceCriteria that = (CompetenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(epreuvesId, that.epreuvesId) &&
            Objects.equals(expertsId, that.expertsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, epreuvesId, expertsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompetenceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalEpreuvesId().map(f -> "epreuvesId=" + f + ", ").orElse("") +
            optionalExpertsId().map(f -> "expertsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
