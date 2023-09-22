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
            case ABOVE_HIGH:
            case ABOVE_AVERAGE_HIGH:
            case AS_EXPECTED_HIGH:
            case LOWER_EXPECTED_HIGH:
                result = 3;
                break;
            case MEDIUM:
            case AVERAGE_MEDIUM:
            case AS_EXPECTED_MEDIUM:
            case AS_EXPECTED_TWO_MEDIUM:
                result = 2;
                break;
            case LOW:
            case VERY_LOW:
            case LOWER_EXPECTED_LOW:
            case AS_EXPECTED_LOW:
            case HIGHER_EXPECTED_LOW:
            case AS_EXPECTED_TWO_LOW:
            case HIGHER_EXPECTED_TWO_LOW:
                result = 1;
                break;
            default:
                result = 0;
                break;
        }

        return result;
    }

    public static String ratingIntToText(Integer rating) {

        if (rating == null || rating <= 0) return "";

        String result;

        switch (rating) {
            case 1:
                result = "Low";
                break;
            case 2:
                result = "Medium";
                break;
            case 3:
                result = "High";
                break;
            default:
                result = "";
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
