package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.cccm.api.openapi.model.FormList;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class GetFormsTest {

    @Inject
    FormsApiServiceImpl sut;

    @Test
    @DisplayName("200: should return forms")
    public void testGetFormsEndpoint() {

        FormList result = sut.getForms();

        Assertions.assertNull(result);

    }

}
