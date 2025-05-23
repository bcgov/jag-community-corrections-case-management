package ca.bc.gov.open.jag.api.model.data;

public class Intervention {

    private String interventionComment;
    private String editKey;
    private String question;
    private String comment;
    private String interventionType;
    private String value;
    private String key;

    public String getInterventionComment() {
        return interventionComment;
    }

    public void setInterventionComment(String interventionComment) {
        this.interventionComment = interventionComment;
    }

    public String getEditKey() {
        return editKey;
    }

    public void setEditKey(String editKey) {
        this.editKey = editKey;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getInterventionType() {
        return interventionType;
    }

    public void setInterventionType(String interventionType) {
        this.interventionType = interventionType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
