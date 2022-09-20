package ca.bc.gov.open.jag.api.util;

import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Designation;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class MappingUtils {

    private MappingUtils() {
    }

    private static final Logger logger = Logger.getLogger(String.valueOf(MappingUtils.class));

    public static BigDecimal calculateAge(String birthDate) {
        if (StringUtils.isBlank(birthDate)) {
            return BigDecimal.ZERO;
        }
        try {
            LocalDate localBirthDate = LocalDate.parse(birthDate);

            return new BigDecimal(ChronoUnit.YEARS.between(localBirthDate, LocalDate.now()));

        } catch (Exception e) {
            logger.severe("Date mapping error: " + birthDate);
            logger.severe(e.getMessage());
            return BigDecimal.ZERO;
        }

    }

    public static Boolean yesNoToBool(String input) {

        return StringUtils.equalsIgnoreCase(input, "Y");

    }

    public static Boolean isExpired(Date inDate) {

        if (inDate == null) {
            return false;
        }

        return inDate.toLocalDate().isAfter(LocalDate.now());

    }

    public static List<Designation> createDesignations(String iaStatus, String popDesignation, String icayraSecurity, String icayraSecurityStatus) {

        List<Designation> designations = new ArrayList<>();
        //TODO this is a guess
        if (StringUtils.isNoneBlank(iaStatus)) {
            designations.add(createDesignation(iaStatus));
        }

        if (StringUtils.isNoneBlank(popDesignation)) {
            designations.add(createDesignation(popDesignation));
        }

        if (StringUtils.isNoneBlank(icayraSecurity)) {
            designations.add(createDesignation(icayraSecurity));
        }

        if (StringUtils.isNoneBlank(icayraSecurityStatus)) {
            designations.add(createDesignation(icayraSecurityStatus));
        }

        return designations;

    }

    public static List<Address> stringToAddressList(String address) {

        if (StringUtils.isBlank(address)) {
            return new ArrayList<>();
        }

        Address address1 = new Address();
        address1.setFullAddress(address);
        address1.setPrimary(true);
        address1.setExpired(false);
        return Collections.singletonList(address1);

    }

    private static Designation createDesignation(String type) {
        Designation designation = new Designation();
        designation.setType(type);
        return designation;
    }
}
