package com.devdouanla.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuestionCriteriaTest {

    @Test
    void newQuestionCriteriaHasAllFiltersNullTest() {
        var questionCriteria = new QuestionCriteria();
        assertThat(questionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void questionCriteriaFluentMethodsCreatesFiltersTest() {
        var questionCriteria = new QuestionCriteria();

        setAllFilters(questionCriteria);

        assertThat(questionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void questionCriteriaCopyCreatesNullFilterTest() {
        var questionCriteria = new QuestionCriteria();
        var copy = questionCriteria.copy();

        assertThat(questionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(questionCriteria)
        );
    }

    @Test
    void questionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var questionCriteria = new QuestionCriteria();
        setAllFilters(questionCriteria);

        var copy = questionCriteria.copy();

        assertThat(questionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(questionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var questionCriteria = new QuestionCriteria();

        assertThat(questionCriteria).hasToString("QuestionCriteria{}");
    }

    private static void setAllFilters(QuestionCriteria questionCriteria) {
        questionCriteria.id();
        questionCriteria.enonce();
        questionCriteria.reponseTexte();
        questionCriteria.points();
        questionCriteria.explication();
        questionCriteria.difficulte();
        questionCriteria.competenceId();
        questionCriteria.distinct();
    }

    private static Condition<QuestionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEnonce()) &&
                condition.apply(criteria.getReponseTexte()) &&
                condition.apply(criteria.getPoints()) &&
                condition.apply(criteria.getExplication()) &&
                condition.apply(criteria.getDifficulte()) &&
                condition.apply(criteria.getCompetenceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuestionCriteria> copyFiltersAre(QuestionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEnonce(), copy.getEnonce()) &&
                condition.apply(criteria.getReponseTexte(), copy.getReponseTexte()) &&
                condition.apply(criteria.getPoints(), copy.getPoints()) &&
                condition.apply(criteria.getExplication(), copy.getExplication()) &&
                condition.apply(criteria.getDifficulte(), copy.getDifficulte()) &&
                condition.apply(criteria.getCompetenceId(), copy.getCompetenceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
