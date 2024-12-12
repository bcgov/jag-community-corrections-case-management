package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.ClientDates;
import ca.bc.gov.open.jag.api.model.validation.Question;
import ca.bc.gov.open.jag.api.model.validation.Validation;
import ca.bc.gov.open.jag.api.util.FormUtils;
import ca.bc.gov.open.jag.cccm.api.openapi.model.InterventionsChecked;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationError;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
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
    private Validation static99rValidation;
    private Validation stableValidation;
    private Validation overallValidation;
    private Validation cmrpValidation;
    private Validation cmrpBasicValidation;
    private Validation casePlanValidation;

    public ValidationServiceImpl(ObjectMapper objectMapper) throws IOException {

        objectMapper.findAndRegisterModules();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        crnaValidation = objectMapper.readValue(loader.getResourceAsStream("/configs/crna_validation_config.json"), Validation.class);
        saraValidation = objectMapper.readValue(loader.getResourceAsStream("/configs/sara_validation_config.json"), Validation.class);
        acuteValidation = objectMapper.readValue(loader.getResourceAsStream("/configs/acute_validation_config.json"), Validation.class);
        static99rValidation = objectMapper.readValue(loader.getResourceAsStream("/configs/static99r_validation_config.json"), Validation.class);
        stableValidation = objectMapper.readValue(loader.getResourceAsStream("/configs/stable_validation_config.json"), Validation.class);
        overallValidation = objectMapper.readValue(loader.getResourceAsStream("/configs/overall_validation_config.json"), Validation.class);
        cmrpBasicValidation = objectMapper.readValue(loader.getResourceAsStream("/configs/cmrp_basic_validation_config.json"), Validation.class);
        casePlanValidation = objectMapper.readValue(loader.getResourceAsStream("/configs/case_plan_validation_config.json"), Validation.class);

    }

    @Override
    public ValidationResult validateCRNA(String answers, List<InterventionsChecked> interventionKeys, Boolean casePlanOmissable) {

        logger.debug("Validate CRNA {}", answers);

        ValidationResult validationResult = new ValidationResult();

        if (!casePlanOmissable) {

            validationResult.setErrors(validate(answers, casePlanValidation, null));

            if (interventionKeys.isEmpty()) {
                ValidationError validationError = new ValidationError();
                validationError.setMessage("At least one intervention is required");
                validationResult.getErrors().add(validationError);
            }

        }
        validationResult.getErrors().addAll(validate(answers, crnaValidation, interventionKeys));

        return validationResult;


    }

    @Override
    public ValidationResult validateSARA(String answers) {

        logger.debug("Validate SARA {}", answers);

        return createValidationResult(validate(answers, saraValidation, Collections.EMPTY_LIST));

    }

    @Override
    public ValidationResult validateACUTE(String answers) {

        logger.debug("Validate ACUTE {}", answers);

        return createValidationResult(validate(answers, acuteValidation, Collections.EMPTY_LIST));

    }

    @Override
    public ValidationResult validateStatic99r(String answers) {

        logger.debug("Validate Static-99r {}", answers);

        return createValidationResult(validate(answers, static99rValidation, Collections.EMPTY_LIST));

    }

    @Override
    public ValidationResult validateStable(String answers) {

        logger.debug("Validate Stable {}", answers);

        return createValidationResult(validate(answers, stableValidation, Collections.EMPTY_LIST));

    }

    @Override
    public ValidationResult validateSOOverall(String answers, Boolean casePlanOmissable) {

        logger.debug("Validate Overall {}", answers);

        ValidationResult validationResult = new ValidationResult();

        if (!casePlanOmissable) {
            validationResult.setErrors(validate(answers, casePlanValidation, null));
        }
        validationResult.getErrors().addAll(validate(answers, overallValidation, Collections.EMPTY_LIST));
        return validationResult;

    }

    @Override
    public ValidationResult validateCMRP(String answers, ClientDates clientDates, List<InterventionsChecked> interventionKeys) {

        logger.debug("Validate CMRP {}", answers);

        ValidationResult validationResult = new ValidationResult();
        LocalDate sixMonths = LocalDate.now().minusMonths(6);
        if (clientDates.getCrnaCompleteDate() == null || clientDates.getCrnaCompleteDate().isBefore(sixMonths)) {
            ValidationError validationError = new ValidationError();
            validationError.setAnswerKey("CRNA Form");
            validationError.setMessage("Must have a completed CRNA-CMP within the last 6 months");
            validationResult.addErrorsItem(validationError);
        }

        if (interventionKeys.isEmpty()) {
            ValidationError validationError = new ValidationError();
            validationError.setAnswerKey("Intervention");
            validationError.setMessage("Form must contain at least one intervention.");
            validationResult.addErrorsItem(validationError);
        }

        validationResult.getErrors().addAll(createValidationResult(validate(answers, cmrpBasicValidation, interventionKeys)).getErrors());

        return validationResult;

    }

    private List<ValidationError> validate(String answers, Validation formValidation, List<InterventionsChecked> interventionKeys) {

        List<ValidationError> errors = new ArrayList<>();

        for(Question question: formValidation.getQuestions()) {
            if (question.getType().equals(CONDITIONAL)) {
                if (isAnswerTriggered(answers, question.getKey(), question.getDependentValues()) && isAnswerValid(answers, question.getDependentKeys())) {
                    errors.add(createValidationError(question.getKey(), question.getMessage()));
                }
            } else if (question.getType().equals(REQUIRED)) {
                if (StringUtils.isBlank(FormUtils.findAnswerByKey(answers, question.getKey()))) {
                    errors.add(createValidationError(question.getKey(), question.getMessage()));
                }
            } else if (question.getType().equals(INTERVENTION_CONDITIONAL)) {
                errors.addAll(validationIntervention(answers, question));
            } else if (question.getType().equals(INTERVENTION_REQUIRED) && !interventionKeys.isEmpty()) {
                errors.addAll(validationInterventionRequired(answers, question, interventionKeys));
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

        String answer = FormUtils.findAnswerByKey(answers, key);
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
            String answer = FormUtils.findAnswerByKey(answers, key);
            if (StringUtils.isBlank(answer)) {
                return true;
            }

        }

        return false;

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

    private List<ValidationError> validationInterventionRequired(String answers, Question question, List<InterventionsChecked> interventionKeys) {

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


        for (InterventionsChecked key: interventionKeys) {
            if (!hasInterventionGrid(jsonData, MessageFormat.format(INTERVENTION_KEY_PATTERN, key.getKey(), INTERVENTION_DATAGRID))) {
                ValidationError validationError = new ValidationError();
                validationError.setAnswerKey(key.getKey());
                validationError.setMessage(question.getMessage());
                validationErrors.add(validationError);
            }
        }

        return validationErrors;

    }

    private boolean hasInterventionGrid(JSONObject jsonData, String key) {
        return (jsonData.has(key));
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
