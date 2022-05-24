package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.error.CCCMException;
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

    private static final LocalDate TEST_COMPLETED_DATE = LocalDate.of(2021, 07, 12);
    private static final LocalDate TEST_CREATED_DATE = LocalDate.of(2021, 03, 12);

    @Inject
    FormsApiImpl sut;

    @Test
    @DisplayName("200: should return form")
    public void testGetClientEndpoint() throws CCCMException {

        FormDetails result = sut.getForm(BigDecimal.ONE);

        Assertions.assertNull(result.getFormId());
        Assertions.assertEquals(TEST_COMPLETED_DATE, result.getCompletedDate());
        Assertions.assertEquals("John, Smith", result.getCreatedBy());
        Assertions.assertEquals(TEST_CREATED_DATE, result.getCreatedDate());
        Assertions.assertEquals("CRNA-CMP", result.getFormType());
        Assertions.assertNull(result.getUpdateDate());


    }

}
