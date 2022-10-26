package ca.bc.gov.open.jag.api.model.service;

import java.util.List;

public class CloneForm {

     private String formType;
     private List<String> ignoreKeys;

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public List<String> getIgnoreKeys() {
        return ignoreKeys;
    }

    public void setIgnoreKeys(List<String> ignoreKeys) {
        this.ignoreKeys = ignoreKeys;
    }

}
