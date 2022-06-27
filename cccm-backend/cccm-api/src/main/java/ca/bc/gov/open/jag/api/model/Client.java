package ca.bc.gov.open.jag.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Client {

    private String CLIENT_NM;
    private String CURRENT_NAME_YN;
    private String GENDER_CD;
    private LocalDate BIRTH_DT;
    private Integer CLIENT_AGE;
    private Integer CS_NO;
    private Integer FPS_NO;
    private String SEALED_YN;
    private BigDecimal CLIE_ID;
    private String GIVEN1_NM;
    private String GIVEN2_NM;
    private String GIVEN3_NM;
    private String SURNAME_NM;
    private String GIVEN1_CON;
    private String GIVEN2_CON;
    private String GIVEN3_CON;
    private String SURNAME_CON;
    private String ACTIVE_YN;
    private String CLTY_CD;

    public String getCLIENT_NM() {
        return CLIENT_NM;
    }

    public void setCLIENT_NM(String CLIENT_NM) {
        this.CLIENT_NM = CLIENT_NM;
    }

    public String getCURRENT_NAME_YN() {
        return CURRENT_NAME_YN;
    }

    public void setCURRENT_NAME_YN(String CURRENT_NAME_YN) {
        this.CURRENT_NAME_YN = CURRENT_NAME_YN;
    }

    public String getGENDER_CD() {
        return GENDER_CD;
    }

    public void setGENDER_CD(String GENDER_CD) {
        this.GENDER_CD = GENDER_CD;
    }

    public LocalDate getBIRTH_DT() {
        return BIRTH_DT;
    }

    public void setBIRTH_DT(LocalDate BIRTH_DT) {
        this.BIRTH_DT = BIRTH_DT;
    }

    public Integer getCLIENT_AGE() {
        return CLIENT_AGE;
    }

    public void setCLIENT_AGE(Integer CLIENT_AGE) {
        this.CLIENT_AGE = CLIENT_AGE;
    }

    public Integer getCS_NO() {
        return CS_NO;
    }

    public void setCS_NO(Integer CS_NO) {
        this.CS_NO = CS_NO;
    }

    public Integer getFPS_NO() {
        return FPS_NO;
    }

    public void setFPS_NO(Integer FPS_NO) {
        this.FPS_NO = FPS_NO;
    }

    public String getSEALED_YN() {
        return SEALED_YN;
    }

    public void setSEALED_YN(String SEALED_YN) {
        this.SEALED_YN = SEALED_YN;
    }

    public BigDecimal getCLIE_ID() {
        return CLIE_ID;
    }

    public void setCLIE_ID(BigDecimal CLIE_ID) {
        this.CLIE_ID = CLIE_ID;
    }

    public String getGIVEN1_NM() {
        return GIVEN1_NM;
    }

    public void setGIVEN1_NM(String GIVEN1_NM) {
        this.GIVEN1_NM = GIVEN1_NM;
    }

    public String getGIVEN2_NM() {
        return GIVEN2_NM;
    }

    public void setGIVEN2_NM(String GIVEN2_NM) {
        this.GIVEN2_NM = GIVEN2_NM;
    }

    public String getGIVEN3_NM() {
        return GIVEN3_NM;
    }

    public void setGIVEN3_NM(String GIVEN3_NM) {
        this.GIVEN3_NM = GIVEN3_NM;
    }

    public String getSURNAME_NM() {
        return SURNAME_NM;
    }

    public void setSURNAME_NM(String SURNAME_NM) {
        this.SURNAME_NM = SURNAME_NM;
    }

    public String getGIVEN1_CON() {
        return GIVEN1_CON;
    }

    public void setGIVEN1_CON(String GIVEN1_CON) {
        this.GIVEN1_CON = GIVEN1_CON;
    }

    public String getGIVEN2_CON() {
        return GIVEN2_CON;
    }

    public void setGIVEN2_CON(String GIVEN2_CON) {
        this.GIVEN2_CON = GIVEN2_CON;
    }

    public String getGIVEN3_CON() {
        return GIVEN3_CON;
    }

    public void setGIVEN3_CON(String GIVEN3_CON) {
        this.GIVEN3_CON = GIVEN3_CON;
    }

    public String getSURNAME_CON() {
        return SURNAME_CON;
    }

    public void setSURNAME_CON(String SURNAME_CON) {
        this.SURNAME_CON = SURNAME_CON;
    }

    public String getACTIVE_YN() {
        return ACTIVE_YN;
    }

    public void setACTIVE_YN(String ACTIVE_YN) {
        this.ACTIVE_YN = ACTIVE_YN;
    }

    public String getCLTY_CD() {
        return CLTY_CD;
    }

    public void setCLTY_CD(String CLTY_CD) {
        this.CLTY_CD = CLTY_CD;
    }

}
