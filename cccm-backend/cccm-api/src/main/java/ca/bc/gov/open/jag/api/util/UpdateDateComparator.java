package ca.bc.gov.open.jag.api.util;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientFormSummary;

import java.time.LocalDate;
import java.util.Comparator;

public class UpdateDateComparator implements Comparator<ClientFormSummary> {
    @Override
    public int compare(final ClientFormSummary e1, final ClientFormSummary e2) {

        LocalDate dateToCompare1 = (e1.getOsuUpdateDate() != null ? e1.getOsuUpdateDate() : (e1.getCompletedDate() != null) ? e1.getCompletedDate() : e1.getCreatedDate());
        LocalDate dateToCompare2 = (e2.getOsuUpdateDate() != null ?  e2.getOsuUpdateDate() : (e2.getCompletedDate() != null) ? e2.getCompletedDate() : e2.getCreatedDate());

        if (dateToCompare1 == null && dateToCompare2 == null) {
            return 0;
        } else if(dateToCompare1 == null) {
            return -1;
        } else if(dateToCompare2 == null) {
            return 1;
        } else {
            return dateToCompare2.compareTo(dateToCompare1);
        }
    }

}
