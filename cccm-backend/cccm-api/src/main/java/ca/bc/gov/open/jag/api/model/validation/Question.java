package ca.bc.gov.open.jag.api.model.validation;

import java.util.List;

public class Question {

    private String key;
    private List<String> dependentKeys;
    private List<String> dependentValues;
    private String message;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getDependentKeys() {
        return dependentKeys;
    }

    public void setDependentKeys(List<String> dependentKeys) {
        this.dependentKeys = dependentKeys;
    }

    public List<String> getDependentValues() {
        return dependentValues;
    }

    public void setDependentValues(List<String> dependentValues) {
        this.dependentValues = dependentValues;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
