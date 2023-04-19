package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientSearchInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Intervention;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class SearchClientInterventionsUsingPOSTTest {

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

        Mockito.when(clientDataService.searchClientFormInterventions(Mockito.any())).thenReturn(createInterventionList());

        ClientSearchInput clientSearchInput = new ClientSearchInput();
        clientSearchInput.setCsNumber(TEST);
        clientSearchInput.setEndDate(LocalDate.now());
        clientSearchInput.setFactors(new ArrayList<>());
        clientSearchInput.setFormModules(new ArrayList<>());
        clientSearchInput.setLocation(BigDecimal.ONE);

        List<Intervention> result = sut.searchClientInterventionsUsingPOST(clientSearchInput);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(BigDecimal.ONE, result.get(0).getId());
        Assertions.assertEquals(TEST,  result.get(0).getType());
        Assertions.assertEquals(TEST,  result.get(0).getFactor());

    }

    @Test
    @TestSecurity(user = "userOidc", roles = "someotherrole")
    @DisplayName("403: throw unauthorized exception")
    public void addTestExceptionBadRole() {

        Assertions.assertThrows(ForbiddenException.class, () -> sut.searchClientInterventionsUsingPOST(new ClientSearchInput()));

    }

    @Test
    @DisplayName("401: throw unauthorized exception")
    public void addTestExceptionNoToken() {

        Assertions.assertThrows(UnauthorizedException.class, () -> sut.searchClientInterventionsUsingPOST(new ClientSearchInput()));

    }

    private List<Intervention> createInterventionList() {

        Intervention intervention = new Intervention();
        intervention.setId(BigDecimal.ONE);
        intervention.setType(TEST);
        intervention.setFactor(TEST);

        return Collections.singletonList(intervention);

    }


}
