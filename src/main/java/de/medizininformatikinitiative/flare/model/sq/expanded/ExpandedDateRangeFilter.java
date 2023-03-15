package de.medizininformatikinitiative.flare.model.sq.expanded;

import de.medizininformatikinitiative.flare.model.fhir.QueryParams;
import de.medizininformatikinitiative.flare.model.sq.Comparator;
import de.medizininformatikinitiative.flare.model.sq.TermCode;

import java.time.LocalDate;

public record ExpandedDateRangeFilter(String searchParameter, LocalDate lowerBound, LocalDate upperBound, TermCode unit)
        implements ExpandedFilter {
    @Override
    public QueryParams toParams() {
        return QueryParams.EMPTY.appendParam(searchParameter, Comparator.GREATER_EQUAL, lowerBound, unit)
                .appendParam(searchParameter, Comparator.LESS_EQUAL, upperBound, unit);
    }
}
