package ca.bc.gov.open.jag.api.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@QuarkusTest
public class AddressTest {

    @Test
    @DisplayName("Test Address Model")
    public void testAddressModel() {

        LocalDate testDate = LocalDate.now();

        Address sut = new Address();

        sut.setId(BigDecimal.ONE);
        sut.setDdtyCd("TEST");
        sut.setAddressLine1Txt("TEST");
        sut.setEffectiveDt(Date.valueOf(testDate));
        sut.setEntDt(Date.valueOf(testDate));
        sut.setEntUserId("TEST");
        sut.setClieId(BigDecimal.ONE);
        sut.setCityCd("TEST");
        sut.setLocaId(BigDecimal.ONE);
        sut.setCoutCd("TEST");
        sut.setPrstCd("TEST");
        sut.setIndiId(BigDecimal.ONE);
        sut.setPostalCodeTxt("TEST");
        sut.setAddressLine2Txt("TEST");
        sut.setAddressLine3Txt("TEST");
        sut.setExpiryDt(Date.valueOf(testDate));
        sut.setUpdDt(Date.valueOf(testDate));
        sut.setUpdUserId("TEST");
        sut.setSealedYn("TEST");

        Assertions.assertEquals(BigDecimal.ONE, sut.getId());
        Assertions.assertEquals("TEST", sut.getDdtyCd());
        Assertions.assertEquals("TEST", sut.getAddressLine1Txt());
        Assertions.assertEquals(Date.valueOf(testDate), sut.getEffectiveDt());
        Assertions.assertEquals(Date.valueOf(testDate), sut.getEntDt());
        Assertions.assertEquals("TEST", sut.getEntUserId());
        Assertions.assertEquals(BigDecimal.ONE, sut.getClieId());
        Assertions.assertEquals("TEST", sut.getCityCd());
        Assertions.assertEquals(BigDecimal.ONE, sut.getLocaId());
        Assertions.assertEquals("TEST", sut.getCoutCd());
        Assertions.assertEquals("TEST", sut.getPrstCd());
        Assertions.assertEquals(BigDecimal.ONE, sut.getIndiId());
        Assertions.assertEquals("TEST", sut.getPostalCodeTxt());
        Assertions.assertEquals("TEST", sut.getAddressLine2Txt());
        Assertions.assertEquals("TEST", sut.getAddressLine3Txt());
        Assertions.assertEquals(Date.valueOf(testDate), sut.getExpiryDt());
        Assertions.assertEquals(Date.valueOf(testDate), sut.getUpdDt());
        Assertions.assertEquals("TEST", sut.getUpdUserId());
        Assertions.assertEquals("TEST", sut.getSealedYn());

    }

}
