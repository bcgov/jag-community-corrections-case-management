package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

@QuarkusTest
public class GetFormTest {

    private static final LocalDate TEST_DATE = LocalDate.of(2021, 12, 14);

    @Inject
    FormsApiServiceImpl sut;

    @Test
    @DisplayName("200: should return client")
    public void testGetClientEndpoint() {

        FormDetails result = sut.getForm(BigDecimal.ONE);

        Assertions.assertEquals(BigDecimal.ONE, result.getFormId());
        Assertions.assertEquals(TEST_DATE, result.getCompletedDate());
        Assertions.assertEquals("Doe, Jane", result.getCreatedBy());
        Assertions.assertEquals(TEST_DATE, result.getCreatedDate());
        Assertions.assertEquals("Initial Assessment", result.getFormType());
        Assertions.assertEquals(TEST_DATE, result.getUpdateDate());

    }

}
