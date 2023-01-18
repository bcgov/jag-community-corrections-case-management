package ca.bc.gov.open.jag.api.service.dataservice.clientForm;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.FormInput;
import ca.bc.gov.open.jag.api.model.service.UpdateForm;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LinkFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.UpdateFormInput;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;

import static ca.bc.gov.open.jag.api.Keys.SARA_FORM_TYPE;

@QuarkusTest
public class LinkFormTest {

    @Inject
    ClientFormSaveService sut;

    @InjectMock
    @RestClient
    ObridgeClientService obridgeClientService;

    @Test
    @DisplayName("Error: Edit is invalid")
    public void testDoesNotHaveOverrideorOwner() {

        Mockito.when(obridgeClientService.createForm(Mockito.any())).thenReturn(BigDecimal.ONE);

        LinkFormInput formInput = new LinkFormInput();
        formInput.setClientFormId(BigDecimal.ONE);
        formInput.setClientNumber("TEST");
        formInput.setLinkedClientFormId(BigDecimal.ONE);
        formInput.setSourcesContacted("TEST");
        formInput.setPlanSummary("TEST");
        formInput.setFormLevelComments("TEST");

        Assertions.assertDoesNotThrow(() -> sut.linkForm(formInput, BigDecimal.ONE));

    }



}
