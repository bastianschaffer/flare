package de.medizininformatikinitiative.flare.model.sq.expanded;

import de.medizininformatikinitiative.flare.model.sq.TermCode;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AgeUtils {

    public static final String searchParam = "birthDate";

    public static LocalDate ageValueToDate(BigDecimal timeValue, TermCode unit){
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

    public static LocalDate equalCaseLowerDate(BigDecimal age, TermCode unit){
        return ageValueToDate(age.add(BigDecimal.ONE), unit).plusDays(1);
    }

    public static LocalDate equalCaseUpperDate(BigDecimal age, TermCode unit){
        return ageValueToDate(age, unit);
    }

}
