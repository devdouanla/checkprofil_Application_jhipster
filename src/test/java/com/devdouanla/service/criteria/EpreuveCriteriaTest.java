package com.devdouanla.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EpreuveCriteriaTest {

    @Test
    void newEpreuveCriteriaHasAllFiltersNullTest() {
        var epreuveCriteria = new EpreuveCriteria();
        assertThat(epreuveCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void epreuveCriteriaFluentMethodsCreatesFiltersTest() {
        var epreuveCriteria = new EpreuveCriteria();

        setAllFilters(epreuveCriteria);

        assertThat(epreuveCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void epreuveCriteriaCopyCreatesNullFilterTest() {
        var epreuveCriteria = new EpreuveCriteria();
        var copy = epreuveCriteria.copy();

        assertThat(epreuveCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(epreuveCriteria)
        );
    }

    @Test
    void epreuveCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var epreuveCriteria = new EpreuveCriteria();
        setAllFilters(epreuveCriteria);

        var copy = epreuveCriteria.copy();

        assertThat(epreuveCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(epreuveCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var epreuveCriteria = new EpreuveCriteria();

        assertThat(epreuveCriteria).hasToString("EpreuveCriteria{}");
    }

    private static void setAllFilters(EpreuveCriteria epreuveCriteria) {
        epreuveCriteria.id();
        epreuveCriteria.titre();
        epreuveCriteria.enonce();
        epreuveCriteria.difficulte();
        epreuveCriteria.duree();
        epreuveCriteria.genereParIA();
        epreuveCriteria.nbInt();
        epreuveCriteria.publie();
        epreuveCriteria.questionsId();
        epreuveCriteria.sessionTestId();
        epreuveCriteria.competenceId();
        epreuveCriteria.distinct();
    }

    private static Condition<EpreuveCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitre()) &&
                condition.apply(criteria.getEnonce()) &&
                condition.apply(criteria.getDifficulte()) &&
                condition.apply(criteria.getDuree()) &&
                condition.apply(criteria.getGenereParIA()) &&
                condition.apply(criteria.getNbInt()) &&
                condition.apply(criteria.getPublie()) &&
                condition.apply(criteria.getQuestionsId()) &&
                condition.apply(criteria.getSessionTestId()) &&
                condition.apply(criteria.getCompetenceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EpreuveCriteria> copyFiltersAre(EpreuveCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitre(), copy.getTitre()) &&
                condition.apply(criteria.getEnonce(), copy.getEnonce()) &&
                condition.apply(criteria.getDifficulte(), copy.getDifficulte()) &&
                condition.apply(criteria.getDuree(), copy.getDuree()) &&
                condition.apply(criteria.getGenereParIA(), copy.getGenereParIA()) &&
                condition.apply(criteria.getNbInt(), copy.getNbInt()) &&
                condition.apply(criteria.getPublie(), copy.getPublie()) &&
                condition.apply(criteria.getQuestionsId(), copy.getQuestionsId()) &&
                condition.apply(criteria.getSessionTestId(), copy.getSessionTestId()) &&
                condition.apply(criteria.getCompetenceId(), copy.getCompetenceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
