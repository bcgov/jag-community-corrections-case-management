package ca.bc.gov.open.jag.api.model.data;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Location {

    private BigDecimal id;
    private String alternateCd;
    private String dsc;

    public BigDecimal getId() {
        return id;
    }

    public String getAlternateCd() {
        return alternateCd;
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

}
