package de.medizininformatikinitiative.flare.model.sq.expanded;

import de.medizininformatikinitiative.flare.model.sq.CalculationException;
import de.medizininformatikinitiative.flare.model.sq.Comparator;
import de.medizininformatikinitiative.flare.model.sq.TermCode;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

public class AgeUtils {

    private static final String searchParam = "birthDate";

    private static Mono<LocalDate> ageValueToDate(BigDecimal timeValue, TermCode unit, Clock clock) {
        int filterValue = timeValue.intValue();
        LocalDate now = LocalDate.now(clock);
        return switch (unit.code()) {
            case "a" -> Mono.just(now.minusYears(filterValue));
            case "mo" -> Mono.just(now.minusMonths(filterValue));
            case "wk" -> Mono.just(now.minusWeeks(filterValue));
            case "d", "h", "min" -> Mono.just(now);
            default -> Mono.error(new CalculationException("Unknown age unit `%s`.".formatted(unit.code())));
        };
    }

    private static Mono<LocalDate> equalCaseLowerDate(BigDecimal age, TermCode unit, Clock clock) {
        return ageValueToDate(age.add(BigDecimal.ONE), unit, clock).map(date -> date.plusDays(1));
    }

    private static Mono<LocalDate> equalCaseUpperDate(BigDecimal age, TermCode unit, Clock clock) {
        return ageValueToDate(age, unit, clock);
    }

    private static Comparator reverseComparator(Comparator comparator) {
        return switch (comparator) {
            case GREATER_THAN -> Comparator.LESS_THAN;
            case LESS_THAN -> Comparator.GREATER_THAN;
            case GREATER_EQUAL -> Comparator.LESS_EQUAL;
            case LESS_EQUAL -> Comparator.GREATER_EQUAL;
            default -> Comparator.EQUAL;
        };
    }

    public static Mono<List<ExpandedFilter>> expandedAgeFilterFromComparator(Comparator comparator, BigDecimal age,
                                                                             TermCode unit, Clock clock) {
        return comparator.equals(Comparator.EQUAL)
                ? equalCaseLowerDate(age, unit, clock).flatMap(lowerBound -> equalCaseUpperDate(age, unit, clock).map(upperBound ->
                List.of(new ExpandedDateRangeFilter(searchParam, lowerBound, upperBound))))
                : ageValueToDate(age, unit, clock).map(date -> List.of(
                new ExpandedDateComparatorFilter(searchParam, reverseComparator(comparator), date)));
    }

    public static Mono<List<ExpandedFilter>> expandedAgeFilterFromRange(BigDecimal lowerBound, BigDecimal upperBound,
                                                                        TermCode unit, Clock clock) {
        return ageValueToDate(lowerBound, unit, clock).flatMap(upperBoundDate -> ageValueToDate(upperBound, unit, clock)
                .map(lowerBoundDate -> List.of(new ExpandedDateRangeFilter(searchParam, lowerBoundDate, upperBoundDate
                ))));
    }

}
