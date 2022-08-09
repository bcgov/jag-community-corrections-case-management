package ca.bc.gov.open.jag.api.service.dataservice.form;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.service.FormRequest;
import ca.bc.gov.open.jag.api.service.FormDataServiceImpl;
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

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
public class GetFormRequestTest {

    FormDataServiceImpl sut;

    @Mock
    private ObjectMapper objectMapperMock;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new FormDataServiceImpl(objectMapperMock);

    }

    @Test
    @DisplayName("Success: should return form data")
    public void getDataTest() throws CCCMException, IOException {

        FormDetails form = new FormDetails();
        form.setFormId(BigDecimal.ONE);

        Mockito.when(objectMapperMock.readValue(any(InputStream.class), eq(FormDetails.class))).thenReturn(form);

        FormDetails result = sut.formRequest(new FormRequest(BigDecimal.ONE, null));

        Assertions.assertEquals(BigDecimal.ONE, result.getFormId());

    }

    @Test
    @DisplayName("Not Found Id: should return not found exception")
    public void getThrowNotFoundException() throws CCCMException {

        Assertions.assertThrows(CCCMException.class, () -> sut.formRequest(new FormRequest(BigDecimal.valueOf(999), null)));

    }

    @Test
    @DisplayName("Not Found Type: should return not found exception")
    public void getByTypeThrowNotFoundException() throws CCCMException {

        Assertions.assertThrows(CCCMException.class, () -> sut.formRequest(new FormRequest(null, "NOTFOUND")));

    }

    @Test
    @DisplayName("Exception: throw cccm exception")
    public void getDataTestException() throws CCCMException, IOException {

        Mockito.when(objectMapperMock.readValue(any(InputStream.class), eq(FormDetails.class))).thenThrow(new IOException());

        Assertions.assertThrows(CCCMException.class, () -> sut.formRequest(new FormRequest(BigDecimal.ONE, null)));

    }

}
