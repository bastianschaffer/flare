package de.medizininformatikinitiative.flare.model.sq;

import de.medizininformatikinitiative.flare.model.mapping.FilterMapping;
import de.medizininformatikinitiative.flare.model.sq.expanded.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.requireNonNull;

public record ComparatorFilterPart(Comparator comparator, BigDecimal value, TermCode unit) implements FilterPart {

    public ComparatorFilterPart {
        requireNonNull(comparator);
        requireNonNull(value);
    }

    /**
     * Returns a comparator filterPart without unit.
     *
     * @param comparator the comparator that should be used in the value comparison
     * @param value      the value that should be used in the value comparison
     * @return the comparator filterPart
     */
    public static ComparatorFilterPart of(Comparator comparator, BigDecimal value) {
        return new ComparatorFilterPart(comparator, value, null);
    }

    /**
     * Returns a comparator filterPart with unit.
     *
     * @param comparator the comparator that should be used in the value comparison
     * @param value      the value that should be used in the value comparison
     * @param unit       the unit of the value
     * @return the comparator filterPart
     */
    public static ComparatorFilterPart of(Comparator comparator, BigDecimal value, TermCode unit) {
        return new ComparatorFilterPart(comparator, value, requireNonNull(unit));
    }

    @Override
    public Mono<List<ExpandedFilter>> expand(FilterMapping filterMapping) {
        if (filterMapping.isAge()) {
            return comparator.equals(Comparator.EQUAL) ? Mono.just(List.of(new ExpandedDateRangeFilter(
                    AgeUtils.searchParam, AgeUtils.equalCaseLowerDate(value, unit),
                    AgeUtils.equalCaseLowerDate(value, unit), unit))) :
                    Mono.just(List.of(new ExpandedDateComparatorFilter(AgeUtils.searchParam, comparator,
                                                                       AgeUtils.ageValueToDate(value, unit), unit)));
        }
        return Mono.just(List.of(new ExpandedComparatorFilter(filterMapping.searchParameter(), comparator, value, unit)));
    }
}
