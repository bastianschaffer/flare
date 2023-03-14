package de.medizininformatikinitiative.flare.model.sq;

import de.medizininformatikinitiative.flare.model.mapping.FilterMapping;
import de.medizininformatikinitiative.flare.model.sq.expanded.ExpandedComparatorFilter;
import de.medizininformatikinitiative.flare.model.sq.expanded.ExpandedFilter;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        if(filterMapping.isAge()){
            if(comparator.equals(Comparator.EQUAL)){
                BigDecimal age = value;
                LocalDate minDate = timeValueToDate(age.add(BigDecimal.ONE)).plusDays(1);
                LocalDate maxDate = timeValueToDate(age);

            }
        }
        return Mono.just(List.of(new ExpandedComparatorFilter(filterMapping.searchParameter(), comparator, value, unit)));
    }

    private LocalDate timeValueToDate(BigDecimal timeValue)  {
        int filterValue = timeValue.intValue();
        //LocalDate date = LocalDate.now(clock);//TODO implement clock for testing here
        LocalDate now = LocalDate.now();
        return switch (unit.code()) {
            case "a" -> now.minusYears(filterValue);
            case "mo" -> now.minusMonths(filterValue);
            case "wk" -> now.minusWeeks(filterValue);
            case "d", "h", "min" -> now;
                    //throw new Exception("d, h, and min as unit of time not implemented");
            default -> now;
        };//TODO: make better exception here
        //TODO: make default
    }
}
