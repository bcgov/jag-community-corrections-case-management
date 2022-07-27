package ca.bc.gov.open.jag.api.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Address {

    private BigDecimal id;
    private String ddtyCd;
    private String addressLine1Txt;
    private Date effectiveDt;
    private Date entDt;
    private String entUserId;
    private BigDecimal clieId;
    private String cityCd;
    private BigDecimal locaId;
    private String coutCd;
    private String prstCd;
    private BigDecimal indiId;
    private String postalCodeTxt;
    private String addressLine2Txt;
    private String addressLine3Txt;
    private Date expiryDt;
    private Date updDt;
    private String updUserId;
    private String sealedYn;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getDdtyCd() {
        return ddtyCd;
    }

    public void setDdtyCd(String ddtyCd) {
        this.ddtyCd = ddtyCd;
    }

    public String getAddressLine1Txt() {
        return addressLine1Txt;
    }

    public void setAddressLine1Txt(String addressLine1Txt) {
        this.addressLine1Txt = addressLine1Txt;
    }

    public Date getEffectiveDt() {
        return effectiveDt;
    }

    public void setEffectiveDt(Date effectiveDt) {
        this.effectiveDt = effectiveDt;
    }

    public Date getEntDt() {
        return entDt;
    }

    public void setEntDt(Date entDt) {
        this.entDt = entDt;
    }

    public String getEntUserId() {
        return entUserId;
    }

    public void setEntUserId(String entUserId) {
        this.entUserId = entUserId;
    }

    public BigDecimal getClieId() {
        return clieId;
    }

    public void setClieId(BigDecimal clieId) {
        this.clieId = clieId;
    }

    public String getCityCd() {
        return cityCd;
    }

    public void setCityCd(String cityCd) {
        this.cityCd = cityCd;
    }

    public BigDecimal getLocaId() {
        return locaId;
    }

    public void setLocaId(BigDecimal locaId) {
        this.locaId = locaId;
    }

    public String getCoutCd() {
        return coutCd;
    }

    public void setCoutCd(String coutCd) {
        this.coutCd = coutCd;
    }

    public String getPrstCd() {
        return prstCd;
    }

    public void setPrstCd(String prstCd) {
        this.prstCd = prstCd;
    }

    public BigDecimal getIndiId() {
        return indiId;
    }

    public void setIndiId(BigDecimal indiId) {
        this.indiId = indiId;
    }

    public String getPostalCodeTxt() {
        return postalCodeTxt;
    }

    public void setPostalCodeTxt(String postalCodeTxt) {
        this.postalCodeTxt = postalCodeTxt;
    }

    public String getAddressLine2Txt() {
        return addressLine2Txt;
    }

    public void setAddressLine2Txt(String addressLine2Txt) {
        this.addressLine2Txt = addressLine2Txt;
    }

    public String getAddressLine3Txt() {
        return addressLine3Txt;
    }

    public void setAddressLine3Txt(String addressLine3Txt) {
        this.addressLine3Txt = addressLine3Txt;
    }

    public Date getExpiryDt() {
        return expiryDt;
    }

    public void setExpiryDt(Date expiryDt) {
        this.expiryDt = expiryDt;
    }

    public Date getUpdDt() {
        return updDt;
    }

    public void setUpdDt(Date updDt) {
        this.updDt = updDt;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    public String getSealedYn() {
        return sealedYn;
    }

    public void setSealedYn(String sealedYn) {
        this.sealedYn = sealedYn;
    }
}
