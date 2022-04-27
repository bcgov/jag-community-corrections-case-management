package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.cccm.api.openapi.model.FormTypeList;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class GetFormTypesTest {

    @Inject
    FormTypesApiServiceImpl sut;

    @Test
    @DisplayName("200: should return form types")
    public void testGetFormTypesEndpoint() {

        FormTypeList result = sut.getFormTypes();

        Assertions.assertEquals(2, result.getItems().size());

    }


}
