package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.validation.Question;
import ca.bc.gov.open.jag.api.model.validation.Validation;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationError;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.bc.gov.open.jag.api.Keys.*;
import static ca.bc.gov.open.jag.api.model.validation.ValidationType.*;

@RequestScoped
public class ValidationServiceImpl implements ValidationService {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(ValidationServiceImpl.class));

    private Validation crnaValidation;
    private Validation saraValidation;
    private Validation acuteValidation;

    public ValidationServiceImpl(ObjectMapper objectMapper) throws IOException {
        objectMapper.findAndRegisterModules();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        crnaValidation = objectMapper.readValue(loader.getResourceAsStream("/configs/crna_validation_config.json"), Validation.class);
        saraValidation = objectMapper.readValue(loader.getResourceAsStream("/configs/sara_validation_config.json"), Validation.class);
        acuteValidation = objectMapper.readValue(loader.getResourceAsStream("/configs/acute_validation_config.json"), Validation.class);

    }

    @Override
    public ValidationResult validateCRNA(String answers) {

        logger.debug("Validate CRNA {}", answers);

        return createValidationResult(validate(answers, crnaValidation));


    }

    @Override
    public ValidationResult validateSARA(String answers) {

        logger.debug("Validate SARA {}", answers);

        return createValidationResult(validate(answers, saraValidation));

    }

    @Override
    public ValidationResult validateACUTE(String answers) {

        logger.debug("Validate ACUTE {}", answers);

        return createValidationResult(validate(answers, acuteValidation));

    }

    private List<ValidationError> validate(String answers, Validation formValidation) {

        List<ValidationError> errors = new ArrayList<>();

        for(Question question: formValidation.getQuestions()) {
            if (question.getType().equals(CONDITIONAL)) {
                if (isAnswerTriggered(answers, question.getKey(), question.getDependentValues()) && isAnswerValid(answers, question.getDependentKeys())) {
                    errors.add(createValidationError(question.getKey(), question.getMessage()));
                }
            } else if (question.getType().equals(REQUIRED)) {
                if (StringUtils.isBlank(findAnswerByKey(answers, question.getKey()))) {
                    errors.add(createValidationError(question.getKey(), question.getMessage()));
                }
            } else if (question.getType().equals(INTERVENTION_CONDITIONAL)) {
                errors.addAll(validationIntervention(answers, question));
            }
        }

        return errors;

    }

    private ValidationError createValidationError(String key, String message) {

        ValidationError validationError = new ValidationError();
        validationError.setAnswerKey(key);
        validationError.setMessage(message);
        return validationError;

    }

    private ValidationResult createValidationResult(List<ValidationError> errors) {

        ValidationResult validationResult = new ValidationResult();
        validationResult.setErrors(errors);
        return validationResult;

    }

    private Boolean isAnswerTriggered(String answers, String key, List<String> possibleResponses) {

        String answer = findAnswerByKey(answers, key);
        return possibleResponses.contains(answer);

    }

    /**
     *
     * @param answers all answers
     * @param keys keys to be validated
     * @return true if any dependent answers failed validation
     */
    private Boolean isAnswerValid(String answers, List<String> keys) {

        for(String key: keys) {
            String answer = findAnswerByKey(answers, key);
            if (StringUtils.isBlank(answer)) {
                return true;
            }

        }

        return false;

    }

    private String findAnswerByKey(String answers, String key) {

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

    private List<ValidationError> validationIntervention(String answers, Question question) {

        if (StringUtils.isBlank(answers)) {
            return Collections.emptyList();
        }

        List<ValidationError> validationErrors = new ArrayList<>();
        JSONObject jsonData = null;
        JSONObject outerData = new JSONObject(answers);
        if (outerData.has(OUTER_DATA_ELEMENT)) {
            jsonData = outerData.getJSONObject(OUTER_DATA_ELEMENT);
        } else {
            jsonData = outerData;
        }

        for (String key: jsonData.keySet()) {
            if (key.contains(INTERVENTION_DATAGRID)) {
                JSONArray jsonArray = jsonData.getJSONArray(key);
                String[] keyParts = key.split("_");
                validationErrors.addAll(validateInterventionGrid(jsonArray, question, keyParts[0]));
            }
        }

        return validationErrors;

    }

    private List<ValidationError> validateInterventionGrid(JSONArray jsonArray, Question question, String keyPart) {

        List<ValidationError> validationErrors = new ArrayList<>();

        for (int i=0; i < jsonArray.length(); i++) {
            String answer = jsonArray.getJSONObject(i).getString(MessageFormat.format(INTERVENTION_KEY_PATTERN, keyPart, question.getKey()));
            if (question.getDependentValues().contains(answer)) {
                for (String requiredAnswerKey: question.getDependentKeys()) {
                    String requiredAnswer = jsonArray.getJSONObject(i).getString(MessageFormat.format(INTERVENTION_KEY_PATTERN, keyPart, requiredAnswerKey));
                    if (StringUtils.isBlank(requiredAnswer)) {
                        validationErrors.add(createValidationError(MessageFormat.format(INTERVENTION_KEY_PATTERN, keyPart, requiredAnswerKey), question.getMessage()));
                    }
                }
            }
        }

        return validationErrors;
    }

}
