package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

@QuarkusTest
public class UpdateFormTest {

    private static final String TEST_VALUE = "TEST";
    private static final LocalDate TEST_DATE = LocalDate.of(2022, 3, 4);

    @Inject
    FormsApiImpl sut;

    @Test
    @TestSecurity(user = "userOidc", roles = "form-update")
    @DisplayName("200: should be updated")
    public void testUpdateFormEndpoint() {

        FormDetails result = sut.updateForm(null, createTestForm());

        Assertions.assertEquals(BigDecimal.ONE, result.getFormId());
        Assertions.assertEquals(TEST_DATE, result.getCompletedDate());
        Assertions.assertEquals(TEST_VALUE, result.getCreatedBy());
        Assertions.assertEquals(TEST_DATE, result.getCreatedDate());
        Assertions.assertEquals(TEST_VALUE, result.getFormType());
        Assertions.assertEquals(TEST_DATE, result.getUpdateDate());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void updateTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.updateForm(null,null));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void updateTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.updateForm(null,null));

    }

    private FormDetails createTestForm() {
        FormDetails formDetails = new FormDetails();

        formDetails.setCompletedDate(TEST_DATE);
        formDetails.setCreatedBy(TEST_VALUE);
        formDetails.setCreatedDate(TEST_DATE);
        formDetails.setFormId(BigDecimal.ONE);
        formDetails.setFormType(TEST_VALUE);
        formDetails.setUpdateDate(TEST_DATE);

        return formDetails;
    }
}
