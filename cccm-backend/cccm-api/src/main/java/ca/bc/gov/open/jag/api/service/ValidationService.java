package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.ClientDates;
import ca.bc.gov.open.jag.cccm.api.openapi.model.InterventionsChecked;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ValidationResult;

import java.util.List;

public interface ValidationService {

    /**
     *
     * @param answers answers to be validated
     * @return failed validations for crna
     */
    ValidationResult validateCRNA(String answers, List<InterventionsChecked> interventionKeys, Boolean casePlanNotRequired);

    /**
     *
     * @param answers answers to be validated
     * @return failed validations for sara
     */
    ValidationResult validateSARA(String answers);

    /**
     *
     * @param answers answers to be validated
     * @return failed validations for acute
     */
    ValidationResult validateACUTE(String answers);

    /**
     *
     * @param answers answers to be validated
     * @return failed validations for static 99 r
     */
    ValidationResult validateStatic99r(String answers);

    /**
     *
     * @param answers answers to be validated
     * @return failed validations for stable
     */
    ValidationResult validateStable(String answers);

    /**
     *
     * @param answers answers to be validated
     * @return failed validations for Overall
     */
    ValidationResult validateSOOverall(String answers, String interventions, Boolean casePlanNotRequired);

    ValidationResult validateCMRP(String answers, ClientDates clientDates, List<InterventionsChecked> interventionKeys);

}
