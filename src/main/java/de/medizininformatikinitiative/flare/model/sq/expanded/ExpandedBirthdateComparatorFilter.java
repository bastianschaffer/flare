package de.medizininformatikinitiative.flare.model.sq.expanded;

import de.medizininformatikinitiative.flare.model.fhir.QueryParams;
import de.medizininformatikinitiative.flare.model.sq.Comparator;
import de.medizininformatikinitiative.flare.model.sq.TermCode;

import java.time.LocalDate;

public record ExpandedBirthdateComparatorFilter(Comparator comparator, LocalDate birthdate, TermCode unit) implements  ExpandedFilter{
    @Override
    public QueryParams toParams() {
        return QueryParams.EMPTY.appendParam("birthdate", comparator, birthdate, unit);
    }
}
