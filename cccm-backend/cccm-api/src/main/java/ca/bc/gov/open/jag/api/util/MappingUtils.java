package ca.bc.gov.open.jag.api.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

public class MappingUtils {

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

}
