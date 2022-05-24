package ca.bc.gov.open.jag.api.service.dataservice;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.service.DataServiceImpl;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
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

import static org.mockito.ArgumentMatchers.*;

@QuarkusTest
public class GetFormByIdTest {

    DataServiceImpl sut;

    @Mock
    private ObjectMapper objectMapperMock;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new DataServiceImpl(objectMapperMock);

    }

    @Test
    @DisplayName("Success: should return form data")
    public void getDataTest() throws CCCMException, IOException {

        FormDetails form = new FormDetails();
        form.setFormId(BigDecimal.ONE);

        Mockito.when(objectMapperMock.readValue(any(File.class), eq(FormDetails.class))).thenReturn(form);

        FormDetails result = sut.getFormById(BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result.getFormId());

    }

    @Test
    @DisplayName("Not Found: should return not found exception")
    public void getThrowNotFoundException() throws CCCMException {

        Assertions.assertThrows(CCCMException.class, () -> sut.getFormById(BigDecimal.valueOf(999)));

    }

    @Test
    @DisplayName("Exception: throw cccm exception")
    public void getDataTestException() throws CCCMException, IOException {

        Mockito.when(objectMapperMock.readValue(any(File.class), eq(FormDetails.class))).thenThrow(new IOException());

        Assertions.assertThrows(CCCMException.class, () -> sut.getFormById(BigDecimal.ONE));

    }

}
