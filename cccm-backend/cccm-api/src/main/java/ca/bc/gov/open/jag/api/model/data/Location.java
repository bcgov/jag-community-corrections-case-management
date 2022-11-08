package ca.bc.gov.open.jag.api.model.data;

import java.math.BigDecimal;

public class Location {

    private BigDecimal id;
    private String lotyCd;
    private String alternateCd;
    private String dsc;

    public BigDecimal getId() {
        return id;
    }

    public String getDsc() {
        return dsc;
    }


    public void setId(BigDecimal id) {
        this.id = id;
    }

    public void setAlternateCd(String alternateCd) {
        this.alternateCd = alternateCd;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public String getlotyCd() {
        return lotyCd;
    }

    public void setlotyCd(String lotyId) {
        this.lotyCd = lotyId;
    }

    public String getAlternateCd() {
        return alternateCd;
    }
}
