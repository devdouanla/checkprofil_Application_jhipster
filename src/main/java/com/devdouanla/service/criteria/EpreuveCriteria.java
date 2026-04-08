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
 * Criteria class for the {@link com.devdouanla.domain.Epreuve} entity. This class is used
 * in {@link com.devdouanla.web.rest.EpreuveResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /epreuves?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EpreuveCriteria implements Serializable, Criteria {

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

    private StringFilter titre;

    private StringFilter enonce;

    private DifficulteFilter difficulte;

    private IntegerFilter duree;

    private BooleanFilter genereParIA;

    private IntegerFilter nbInt;

    private BooleanFilter publie;

    private LongFilter questionsId;

    private LongFilter sessionTestId;

    private LongFilter competenceId;

    private Boolean distinct;

    public EpreuveCriteria() {}

    public EpreuveCriteria(EpreuveCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titre = other.optionalTitre().map(StringFilter::copy).orElse(null);
        this.enonce = other.optionalEnonce().map(StringFilter::copy).orElse(null);
        this.difficulte = other.optionalDifficulte().map(DifficulteFilter::copy).orElse(null);
        this.duree = other.optionalDuree().map(IntegerFilter::copy).orElse(null);
        this.genereParIA = other.optionalGenereParIA().map(BooleanFilter::copy).orElse(null);
        this.nbInt = other.optionalNbInt().map(IntegerFilter::copy).orElse(null);
        this.publie = other.optionalPublie().map(BooleanFilter::copy).orElse(null);
        this.questionsId = other.optionalQuestionsId().map(LongFilter::copy).orElse(null);
        this.sessionTestId = other.optionalSessionTestId().map(LongFilter::copy).orElse(null);
        this.competenceId = other.optionalCompetenceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EpreuveCriteria copy() {
        return new EpreuveCriteria(this);
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

    public StringFilter getTitre() {
        return titre;
    }

    public Optional<StringFilter> optionalTitre() {
        return Optional.ofNullable(titre);
    }

    public StringFilter titre() {
        if (titre == null) {
            setTitre(new StringFilter());
        }
        return titre;
    }

    public void setTitre(StringFilter titre) {
        this.titre = titre;
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

    public IntegerFilter getDuree() {
        return duree;
    }

    public Optional<IntegerFilter> optionalDuree() {
        return Optional.ofNullable(duree);
    }

    public IntegerFilter duree() {
        if (duree == null) {
            setDuree(new IntegerFilter());
        }
        return duree;
    }

    public void setDuree(IntegerFilter duree) {
        this.duree = duree;
    }

    public BooleanFilter getGenereParIA() {
        return genereParIA;
    }

    public Optional<BooleanFilter> optionalGenereParIA() {
        return Optional.ofNullable(genereParIA);
    }

    public BooleanFilter genereParIA() {
        if (genereParIA == null) {
            setGenereParIA(new BooleanFilter());
        }
        return genereParIA;
    }

    public void setGenereParIA(BooleanFilter genereParIA) {
        this.genereParIA = genereParIA;
    }

    public IntegerFilter getNbInt() {
        return nbInt;
    }

    public Optional<IntegerFilter> optionalNbInt() {
        return Optional.ofNullable(nbInt);
    }

    public IntegerFilter nbInt() {
        if (nbInt == null) {
            setNbInt(new IntegerFilter());
        }
        return nbInt;
    }

    public void setNbInt(IntegerFilter nbInt) {
        this.nbInt = nbInt;
    }

    public BooleanFilter getPublie() {
        return publie;
    }

    public Optional<BooleanFilter> optionalPublie() {
        return Optional.ofNullable(publie);
    }

    public BooleanFilter publie() {
        if (publie == null) {
            setPublie(new BooleanFilter());
        }
        return publie;
    }

    public void setPublie(BooleanFilter publie) {
        this.publie = publie;
    }

    public LongFilter getQuestionsId() {
        return questionsId;
    }

    public Optional<LongFilter> optionalQuestionsId() {
        return Optional.ofNullable(questionsId);
    }

    public LongFilter questionsId() {
        if (questionsId == null) {
            setQuestionsId(new LongFilter());
        }
        return questionsId;
    }

    public void setQuestionsId(LongFilter questionsId) {
        this.questionsId = questionsId;
    }

    public LongFilter getSessionTestId() {
        return sessionTestId;
    }

    public Optional<LongFilter> optionalSessionTestId() {
        return Optional.ofNullable(sessionTestId);
    }

    public LongFilter sessionTestId() {
        if (sessionTestId == null) {
            setSessionTestId(new LongFilter());
        }
        return sessionTestId;
    }

    public void setSessionTestId(LongFilter sessionTestId) {
        this.sessionTestId = sessionTestId;
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
        final EpreuveCriteria that = (EpreuveCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titre, that.titre) &&
            Objects.equals(enonce, that.enonce) &&
            Objects.equals(difficulte, that.difficulte) &&
            Objects.equals(duree, that.duree) &&
            Objects.equals(genereParIA, that.genereParIA) &&
            Objects.equals(nbInt, that.nbInt) &&
            Objects.equals(publie, that.publie) &&
            Objects.equals(questionsId, that.questionsId) &&
            Objects.equals(sessionTestId, that.sessionTestId) &&
            Objects.equals(competenceId, that.competenceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            titre,
            enonce,
            difficulte,
            duree,
            genereParIA,
            nbInt,
            publie,
            questionsId,
            sessionTestId,
            competenceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EpreuveCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitre().map(f -> "titre=" + f + ", ").orElse("") +
            optionalEnonce().map(f -> "enonce=" + f + ", ").orElse("") +
            optionalDifficulte().map(f -> "difficulte=" + f + ", ").orElse("") +
            optionalDuree().map(f -> "duree=" + f + ", ").orElse("") +
            optionalGenereParIA().map(f -> "genereParIA=" + f + ", ").orElse("") +
            optionalNbInt().map(f -> "nbInt=" + f + ", ").orElse("") +
            optionalPublie().map(f -> "publie=" + f + ", ").orElse("") +
            optionalQuestionsId().map(f -> "questionsId=" + f + ", ").orElse("") +
            optionalSessionTestId().map(f -> "sessionTestId=" + f + ", ").orElse("") +
            optionalCompetenceId().map(f -> "competenceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
