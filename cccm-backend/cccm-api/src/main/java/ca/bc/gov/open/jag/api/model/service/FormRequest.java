package ca.bc.gov.open.jag.api.model.service;

import java.math.BigDecimal;

public class FormRequest {

    private BigDecimal formId;
    private String formType;

    public  FormRequest(BigDecimal formId, String formType) {

        this.formId = formId;
        this.formType = formType;

    }

    public BigDecimal getFormId() {
        return formId;
    }

    public String getFormType() {
        return formType;
    }
}
