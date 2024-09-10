package ca.bc.gov.open.jag.api.util;

import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Designation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static ca.bc.gov.open.jag.api.Keys.DAYS_TILL_LOCKED;

public class MappingUtils {

    private MappingUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(MappingUtils.class));

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static BigDecimal calculateAge(String birthDate) {
        if (StringUtils.isBlank(birthDate)) {
            return BigDecimal.ZERO;
        }
        try {

            if (birthDate.length() == 10) {

                LocalDate localBirthDate = LocalDate.parse(birthDate, DATE_TIME_FORMATTER);

                return new BigDecimal(ChronoUnit.YEARS.between(localBirthDate, LocalDate.now()));
            } else {
                DateTimeFormatter inputDtf = DateTimeFormatter.ofPattern("MMMM d, u", Locale.ENGLISH);
                LocalDate localBirthDate = LocalDate.parse(normalizeDate(birthDate), inputDtf);
                return new BigDecimal(ChronoUnit.YEARS.between(localBirthDate, LocalDate.now()));
            }

        } catch (Exception e) {
            logger.error("Date mapping error: {}", birthDate);
            logger.error(e.getMessage());
            return BigDecimal.ZERO;
        }

    }

    public static Boolean calculateLocked(LocalDate createdDate) {
        if (createdDate == null) {
            return false;
        }
        try {

            return createdDate.plusDays(DAYS_TILL_LOCKED).isBefore(LocalDate.now());

        } catch (Exception e) {
            logger.error("Locked form calculation error: {}", createdDate);
            logger.error(e.getMessage());
            return false;
        }

    }

    public static String formatDate(String input) {

        if (StringUtils.isBlank(input)) {
            return "";
        }
        try {
        if (input.length() == 10) {

            LocalDate localBirthDate = LocalDate.parse(input);

            return localBirthDate.toString();

        } else {

            DateTimeFormatter inputDtf = DateTimeFormatter.ofPattern("MMMM d, u", Locale.ENGLISH);
            LocalDate localBirthDate = LocalDate.parse(normalizeDate(input), inputDtf);
            return localBirthDate.toString();

        }

        } catch (Exception e) {
            logger.error("Date formatting error: {}", input);
            logger.error(e.getMessage());
            return "";
        }

    }

    public static Boolean yesNoToBool(String input) {

        return StringUtils.equalsIgnoreCase(input, "Y");

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

    public static List<Designation> createUcmpDesignations(String ucmpDesignation) {

        List<Designation> designations = new ArrayList<>();

        if (StringUtils.isNoneBlank(ucmpDesignation)) {
            designations.add(createDesignation(ucmpDesignation));
        }

        return designations;

    }

    public static List<Address> stringToAddressList(String address, String addressType, String addressExpiry) {

        if (StringUtils.isBlank(address)) {
            return new ArrayList<>();
        }

        Address address1 = new Address();
        address1.setFullAddress(address);
        address1.setType(addressType);
        address1.setPrimary(true);
        address1.setExpired(isExpired(addressExpiry));
        return Collections.singletonList(address1);

    }

    private static Designation createDesignation(String type) {
        Designation designation = new Designation();
        designation.setType(type);
        return designation;
    }

    public static Boolean isExpired(String expiry) {
        if (StringUtils.isBlank(expiry)) {
            return false;
        }

        if (expiry.length() <= 10) {
            return Date.valueOf(expiry).toLocalDate().isBefore(LocalDate.now());
        } else {
            return LocalDate.parse(StringUtils.truncate(expiry, 10)).isBefore(LocalDate.now());
        }

    }

    private static String normalizeDate(String input) {
        return StringUtils.normalizeSpace(StringUtils.capitalize(input.toLowerCase()));
    }

}
