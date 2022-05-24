package ca.bc.gov.open.jag.api.service.dataservice;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.service.DataServiceImpl;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SideCards;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
public class GetSideCardsTest {

    DataServiceImpl sut;

    @Mock
    private ObjectMapper objectMapperMock;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new DataServiceImpl(objectMapperMock);

    }

    @Test
    @DisplayName("Success: should return side card data")
    public void getDataTest() throws CCCMException, IOException {


        SideCards sideCards = new SideCards();
        sideCards.setDisplay("display");

        Mockito.when(objectMapperMock.readValue(any(File.class), eq(SideCards.class))).thenReturn(sideCards);

        //Currently this test is a bit redundant 
        SideCards result = sut.getSideCardsById(BigDecimal.ONE);

        Assertions.assertEquals("display", result.getDisplay());

    }

    @Test
    @DisplayName("Exception: throw cccm exception")
    public void getDataTestException() throws CCCMException, IOException {

        Mockito.when(objectMapperMock.readValue(any(File.class), eq(SideCards.class))).thenThrow(new IOException());

        Assertions.assertThrows(CCCMException.class, () -> sut.getSideCardsById(BigDecimal.ONE));

    }

}
