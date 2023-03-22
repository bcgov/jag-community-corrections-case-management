package ca.bc.gov.open.jag.api.model.data;

public class Answer {

    private String text;
    private String description;
    private String comment;
    private int sequence;
    private int section;

    public String getKey () {
        return String.format("S%02dQ%02d", section, sequence);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

}
