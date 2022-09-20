package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormQuestionAnswer;
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
public class UpdateQuestionAnswerTest {

    @Inject
    FormsApiImpl sut;

    @Test
    @TestSecurity(user = "userOidc", roles = "form-update")
    @DisplayName("200: form added")
    public void testClientsAddEndpoint() {

        FormQuestionAnswer request = new FormQuestionAnswer();
        request.setFormQuestionId(BigDecimal.ONE);
        request.setFormAnswerId(BigDecimal.ONE);

        FormQuestionAnswer result = sut.updateQuestionAnswer(BigDecimal.ONE,null, request);

        Assertions.assertNull(result);

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "form-update")
    @DisplayName("400: throw cccm exception")
    public void addTestExceptionBadRequestRole() {

        Assertions.assertThrows(CCCMException.class, () -> sut.updateQuestionAnswer(BigDecimal.ONE,null, new FormQuestionAnswer()));

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.updateQuestionAnswer(BigDecimal.ONE, null,new FormQuestionAnswer()));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.updateQuestionAnswer(BigDecimal.ONE,null, new FormQuestionAnswer()));

    }

}
