package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.validation.Question;
import ca.bc.gov.open.jag.api.model.validation.Validation;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationError;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

        List<ValidationError> failedValidations = new ArrayList<>();

        for(Question question: crnaValidation.getQuestions()) {

            if (!validate(answers, question)) {
                failedValidations.add(createValidationError(question.getKey(), question.getMessage()));
            }

        }

        return createValidationResult(failedValidations);

    }

    @Override
    public ValidationResult validateSARA(String answers) {

        List<ValidationError> failedValidations = new ArrayList<>();

        for(Question question: saraValidation.getQuestions()) {

            if (validate(answers, question).equals(true)) {
                failedValidations.add(createValidationError(question.getKey(), question.getMessage()));
            }

        }

        return createValidationResult(failedValidations);

    }

    private Boolean validate(String answers, Question question) {

        String answer = findAnswerByKey(answers, question.getKey());

        switch (question.getValidationType()) {
            case CONDITIONAL: {
                return (isAnswerTriggered(answers, question.getDependentKeys(), question.getDependentValues()) && StringUtils.isBlank(answer));
            }
            case REQUIRED: {
                return StringUtils.isBlank(answer);
            }
            default: {
                return false;
            }
        }

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

}
