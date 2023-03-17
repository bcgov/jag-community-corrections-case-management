package ca.bc.gov.open.jag.api.util;

import org.apache.commons.lang3.StringUtils;

import static ca.bc.gov.open.jag.api.Keys.*;

public class FormUtil {

    private FormUtil() {}

    public static Integer ratingToInteger(String rating) {

        if (StringUtils.isBlank(rating)) {
            return -1;
        }

        int result;

        switch (rating) {
            case HIGH:
                result = 3;
                break;
            case MEDIUM:
                result = 2;
                break;
            case LOW:
                result = 1;
                break;
            default:
                result = 0;
                break;
        }

        return result;
    }

}
