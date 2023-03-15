package de.medizininformatikinitiative.flare.model.sq.expanded;

import de.medizininformatikinitiative.flare.model.fhir.QueryParams;
import de.medizininformatikinitiative.flare.model.sq.TermCode;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static de.medizininformatikinitiative.flare.model.sq.Comparator.LESS_THAN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class AgeFilterTest {


    @Test
    void age_withoutUnit() {
        TermCode unit = new TermCode("http://snomed.info/sct", "a", "years");
        ExpandedDateComparatorFilter comparatorFilter = new ExpandedDateComparatorFilter(
                "birthDate", LESS_THAN, LocalDate.parse("1999-01-02"), unit);


        assertThat(comparatorFilter.toParams()).isEqualTo(QueryParams.of("birthDate",
                                                                         LESS_THAN.toString() + "1999-01-02" +
                                                                                 "|" + unit.system() + "|" + unit.code()));
    }
}
