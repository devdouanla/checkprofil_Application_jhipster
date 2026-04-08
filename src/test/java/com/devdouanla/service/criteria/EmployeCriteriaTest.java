package com.devdouanla.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmployeCriteriaTest {

    @Test
    void newEmployeCriteriaHasAllFiltersNullTest() {
        var employeCriteria = new EmployeCriteria();
        assertThat(employeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void employeCriteriaFluentMethodsCreatesFiltersTest() {
        var employeCriteria = new EmployeCriteria();

        setAllFilters(employeCriteria);

        assertThat(employeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void employeCriteriaCopyCreatesNullFilterTest() {
        var employeCriteria = new EmployeCriteria();
        var copy = employeCriteria.copy();

        assertThat(employeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(employeCriteria)
        );
    }

    @Test
    void employeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var employeCriteria = new EmployeCriteria();
        setAllFilters(employeCriteria);

        var copy = employeCriteria.copy();

        assertThat(employeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(employeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var employeCriteria = new EmployeCriteria();

        assertThat(employeCriteria).hasToString("EmployeCriteria{}");
    }

    private static void setAllFilters(EmployeCriteria employeCriteria) {
        employeCriteria.id();
        employeCriteria.nom();
        employeCriteria.dateRecrutement();
        employeCriteria.evaluationsId();
        employeCriteria.posteId();
        employeCriteria.distinct();
    }

    private static Condition<EmployeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getDateRecrutement()) &&
                condition.apply(criteria.getEvaluationsId()) &&
                condition.apply(criteria.getPosteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EmployeCriteria> copyFiltersAre(EmployeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getDateRecrutement(), copy.getDateRecrutement()) &&
                condition.apply(criteria.getEvaluationsId(), copy.getEvaluationsId()) &&
                condition.apply(criteria.getPosteId(), copy.getPosteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
