package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

@QuarkusTest
public class GetFormTest {

    @Inject
    FormsApiServiceImpl sut;

    @Test
    @DisplayName("200: should return client")
    public void testGetClientEndpoint() {

        FormDetails result = sut.getForm(BigDecimal.ONE);

        Assertions.assertNull(result);

    }

}
