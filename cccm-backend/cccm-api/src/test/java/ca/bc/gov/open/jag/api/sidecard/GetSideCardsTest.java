package ca.bc.gov.open.jag.api.sidecard;

import ca.bc.gov.open.jag.cccm.api.openapi.model.SideCards;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class GetSideCardsTest {

    @Inject
    SideCardsApiImpl sut;


    @Test
    @DisplayName("200: should return side card")
    public void getSideCardSuccess() {

        SideCards result =  sut.getSideCards();

        Assertions.assertEquals("form", result.getDisplay());

    }

}
