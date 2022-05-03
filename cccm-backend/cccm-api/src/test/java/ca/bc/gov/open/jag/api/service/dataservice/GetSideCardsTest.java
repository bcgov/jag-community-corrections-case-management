package ca.bc.gov.open.jag.api.service.dataservice;

import ca.bc.gov.open.jag.api.service.DataServiceImpl;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SideCards;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

@QuarkusTest
public class GetSideCardsTest {

    @Inject
    DataServiceImpl sut;

    @Test
    @DisplayName("Success: should return side card data")
    public void getDataTest() {
        //Currently this test is a bit redundant 
        SideCards result = sut.getSideCardsById(BigDecimal.ONE);

        Assertions.assertEquals("form", result.getDisplay());

    }
}
