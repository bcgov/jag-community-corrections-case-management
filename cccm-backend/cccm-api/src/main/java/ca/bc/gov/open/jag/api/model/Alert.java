package ca.bc.gov.open.jag.api.model;

import java.util.Date;

public class Alert {

    private String commentTxt;
    private Date effectiveDt;

    public String getCommentTxt() {
        return commentTxt;
    }

    public void setCommentTxt(String commentTxt) {
        this.commentTxt = commentTxt;
    }

    public Date getEffectiveDt() {
        return effectiveDt;
    }

    public void setEffectiveDt(Date effectiveDt) {
        this.effectiveDt = effectiveDt;
    }
}
