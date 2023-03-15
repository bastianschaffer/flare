package de.medizininformatikinitiative.flare.model.sq.expanded;

import de.medizininformatikinitiative.flare.model.fhir.QueryParams;
import de.medizininformatikinitiative.flare.model.sq.Comparator;

import java.time.LocalDate;

public record ExpandedDateComparatorFilter(String searchParameter, Comparator comparator, LocalDate birthdate)
        implements ExpandedFilter {

    @Override
    public QueryParams toParams() {
        return QueryParams.EMPTY.appendParam(searchParameter, comparator, birthdate);
    }
}
