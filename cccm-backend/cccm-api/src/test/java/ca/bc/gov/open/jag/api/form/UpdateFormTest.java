package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class UpdateFormTest {

    @Inject
    FormsApiServiceImpl sut;

    @Test
    @DisplayName("200: should be updated")
    public void testUpdateFormEndpoint() {

        FormDetails result = sut.updateForm(new FormDetails());

        Assertions.assertNull(result);

    }

}
