package ca.bc.gov.open.jag.api.form;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

@QuarkusTest
public class DeleteFormTest {

    @Inject
    FormsApiImpl sut;

    @Test
    @TestSecurity(user = "userOidc", roles = "form-delete")
    @DisplayName("200: no error")
    public void testDeleteEndpoint() {

        Assertions.assertDoesNotThrow(() -> sut.deleteForm(BigDecimal.ONE));

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void deleteTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.deleteForm(BigDecimal.ONE));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void deleteTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.deleteForm(BigDecimal.ONE));

    }

}
