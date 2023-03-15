package de.medizininformatikinitiative.flare.model.sq.expanded;

import de.medizininformatikinitiative.flare.model.fhir.QueryParams;

import java.time.LocalDate;

import static de.medizininformatikinitiative.flare.model.sq.Comparator.GREATER_EQUAL;
import static de.medizininformatikinitiative.flare.model.sq.Comparator.LESS_EQUAL;

public record ExpandedDateRangeFilter(String searchParameter, LocalDate lowerBound, LocalDate upperBound)
        implements ExpandedFilter {
    @Override
    public QueryParams toParams() {
        return QueryParams.EMPTY
                .appendParam(searchParameter, GREATER_EQUAL, lowerBound)
                .appendParam(searchParameter, LESS_EQUAL, upperBound);
    }
}
