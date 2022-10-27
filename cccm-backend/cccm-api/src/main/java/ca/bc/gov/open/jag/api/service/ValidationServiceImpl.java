package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.validation.Question;
import ca.bc.gov.open.jag.api.model.validation.Validation;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationError;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static ca.bc.gov.open.jag.api.Keys.INTERVENTION_DATAGRID;
import static ca.bc.gov.open.jag.api.Keys.INTERVENTION_KEY_PATTERN;
import static ca.bc.gov.open.jag.api.model.validation.ValidationType.*;

@RequestScoped
public class ValidationServiceImpl implements ValidationService {

    private Validation crnaValidation;
    private Validation saraValidation;

    public ValidationServiceImpl(ObjectMapper objectMapper) throws IOException {
        objectMapper.findAndRegisterModules();

        crnaValidation = objectMapper.readValue(Paths.get("configs/crna_validation_config.json").toFile(), Validation.class);
        saraValidation = objectMapper.readValue(Paths.get("configs/sara_validation_config.json").toFile(), Validation.class);

    }

    @Override
    public ValidationResult validateCRNA(String answers) {

        return createValidationResult(validate(answers, crnaValidation));

    }

    @Override
    public ValidationResult validateSARA(String answers) {

        return createValidationResult(validate(answers, saraValidation));

    }

    private List<ValidationError> validate(String answers, Validation formValidation) {

        List<ValidationError> errors = new ArrayList<>();

        for(Question question: formValidation.getQuestions()) {
            if (question.getValidationType().equals(CONDITIONAL)) {
                if (isAnswerTriggered(answers, question.getDependentKeys(), question.getDependentValues()) && StringUtils.isBlank(findAnswerByKey(answers, question.getKey()))) {
                    errors.add(createValidationError(question.getKey(), question.getMessage()));
                }
            } else if (question.getValidationType().equals(REQUIRED)) {
                if (StringUtils.isBlank(findAnswerByKey(answers, question.getKey()))) {
                    errors.add(createValidationError(question.getKey(), question.getMessage()));
                }
            } else if (question.getValidationType().equals(INTERVENTION_CONDITIONAL)) {
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

    private Boolean isAnswerTriggered(String answers, List<String> keys, List<String> possibleResponses) {

        for(String key: keys) {
            String answer = findAnswerByKey(answers, key);
            if (possibleResponses.contains(answer)) {
                return true;
            }

        }

        return false;

    }

    private String findAnswerByKey(String answers, String key) {

        JSONObject jsonData = new JSONObject(answers);
        if (jsonData.has(key)) {
            return jsonData.getString(key);
        }

        return null;

    }

    private List<ValidationError> validationIntervention(String answers, Question question) {
        List<ValidationError> validationErrors = new ArrayList<>();
        JSONObject jsonData = new JSONObject(answers);
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
