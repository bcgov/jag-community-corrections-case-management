package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientSearchInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Comment;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class SearchClientCommentsUsingPOSTTest {

    private static final String TEST = "TEST";

    @Inject
    FormsApiImpl sut;

    @InjectMock
    ClientDataService clientDataService;

    @InjectMock
    FormDataService formDataService;

    @InjectMock
    ClientFormSaveService clientFormSaveService;

    @InjectMock
    ValidationService validationService;

    @InjectMock
    JsonWebToken jwt;

    @Test
    @TestSecurity(user = "userOidc", roles = "form-view")
    @DisplayName("200: should search comment")
    public void testGetSuccess() {

        Mockito.when(clientDataService.searchClientFormComments(Mockito.any())).thenReturn(createCommentList());

        ClientSearchInput clientSearchInput = new ClientSearchInput();
        clientSearchInput.setCsNumber(TEST);
        clientSearchInput.setEndDate(LocalDate.now());
        clientSearchInput.setFactors(new ArrayList<>());
        clientSearchInput.setFormModules(new ArrayList<>());
        clientSearchInput.setLocation(BigDecimal.ONE);

        List<Comment> result = sut.searchClientCommentsUsingPOST(clientSearchInput);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(BigDecimal.ONE, result.get(0).getId());
        Assertions.assertEquals(TEST,  result.get(0).getAnswerValue());
        Assertions.assertEquals(TEST,  result.get(0).getValue());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.searchClientCommentsUsingPOST(new ClientSearchInput()));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.searchClientCommentsUsingPOST(new ClientSearchInput()));

    }

    private List<Comment> createCommentList() {

        Comment comment = new Comment();
        comment.setId(BigDecimal.ONE);
        comment.setAnswerValue(TEST);
        comment.setValue(TEST);

        return Collections.singletonList(comment);

    }

}
