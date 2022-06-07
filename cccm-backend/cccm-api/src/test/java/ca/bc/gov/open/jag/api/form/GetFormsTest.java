package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormList;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class GetFormsTest {

    @Inject
    FormsApiImpl sut;

    @Test
    @TestSecurity(user = "userOidc", roles = "data-view")
    @DisplayName("200: should return forms")
    public void testGetFormsEndpoint() throws CCCMException {

        FormList result = sut.getForms();

        Assertions.assertEquals(1, result.getItems().size());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void getTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.getForms());

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void getTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.getForms());

    }

}
