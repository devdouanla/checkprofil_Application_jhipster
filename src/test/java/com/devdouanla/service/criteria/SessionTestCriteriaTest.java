package com.devdouanla.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SessionTestCriteriaTest {

    @Test
    void newSessionTestCriteriaHasAllFiltersNullTest() {
        var sessionTestCriteria = new SessionTestCriteria();
        assertThat(sessionTestCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void sessionTestCriteriaFluentMethodsCreatesFiltersTest() {
        var sessionTestCriteria = new SessionTestCriteria();

        setAllFilters(sessionTestCriteria);

        assertThat(sessionTestCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void sessionTestCriteriaCopyCreatesNullFilterTest() {
        var sessionTestCriteria = new SessionTestCriteria();
        var copy = sessionTestCriteria.copy();

        assertThat(sessionTestCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(sessionTestCriteria)
        );
    }

    @Test
    void sessionTestCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var sessionTestCriteria = new SessionTestCriteria();
        setAllFilters(sessionTestCriteria);

        var copy = sessionTestCriteria.copy();

        assertThat(sessionTestCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(sessionTestCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var sessionTestCriteria = new SessionTestCriteria();

        assertThat(sessionTestCriteria).hasToString("SessionTestCriteria{}");
    }

    private static void setAllFilters(SessionTestCriteria sessionTestCriteria) {
        sessionTestCriteria.id();
        sessionTestCriteria.scoreObtenu();
        sessionTestCriteria.dateDebut();
        sessionTestCriteria.dateFin();
        sessionTestCriteria.resultatId();
        sessionTestCriteria.questionsAskId();
        sessionTestCriteria.reponsesId();
        sessionTestCriteria.evaluationId();
        sessionTestCriteria.epreuveId();
        sessionTestCriteria.distinct();
    }

    private static Condition<SessionTestCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getScoreObtenu()) &&
                condition.apply(criteria.getDateDebut()) &&
                condition.apply(criteria.getDateFin()) &&
                condition.apply(criteria.getResultatId()) &&
                condition.apply(criteria.getQuestionsAskId()) &&
                condition.apply(criteria.getReponsesId()) &&
                condition.apply(criteria.getEvaluationId()) &&
                condition.apply(criteria.getEpreuveId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SessionTestCriteria> copyFiltersAre(SessionTestCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getScoreObtenu(), copy.getScoreObtenu()) &&
                condition.apply(criteria.getDateDebut(), copy.getDateDebut()) &&
                condition.apply(criteria.getDateFin(), copy.getDateFin()) &&
                condition.apply(criteria.getResultatId(), copy.getResultatId()) &&
                condition.apply(criteria.getQuestionsAskId(), copy.getQuestionsAskId()) &&
                condition.apply(criteria.getReponsesId(), copy.getReponsesId()) &&
                condition.apply(criteria.getEvaluationId(), copy.getEvaluationId()) &&
                condition.apply(criteria.getEpreuveId(), copy.getEpreuveId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
