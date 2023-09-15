package ca.bc.gov.open.jag.api.util;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import static ca.bc.gov.open.jag.api.Keys.*;

public class FormUtils {

    private FormUtils() {}

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

    public static String findAnswerByKey(String answers, String key) {

        if (StringUtils.isBlank(answers)) {
            return null;
        }

        JSONObject jsonData = null;
        JSONObject outerData = new JSONObject(answers);
        if (outerData.has(OUTER_DATA_ELEMENT)) {
            jsonData = outerData.getJSONObject(OUTER_DATA_ELEMENT);
        } else {
            jsonData = outerData;
        }

        if (jsonData.has(key)) {
            return jsonData.getString(key);
        }

        return null;

    }

}
