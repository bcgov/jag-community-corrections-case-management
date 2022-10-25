package ca.bc.gov.open.jag.api.model.validation;

public class ValidationError {

    private String answerKey;
    private String message;

    public ValidationError(String answerKey, String message) {
        this.answerKey = answerKey;
        this.message = message;
    }

    public String getAnswerKey() {
        return answerKey;
    }


    public String getMessage() {
        return message;
    }

}
