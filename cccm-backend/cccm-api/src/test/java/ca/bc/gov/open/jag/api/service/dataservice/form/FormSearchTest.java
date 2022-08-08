package ca.bc.gov.open.jag.api.service.dataservice.form;

import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import java.time.LocalDate;

public class FormSearchTest {

    private static final String TEST_STRING = "TEST";
    private static final LocalDate TEST_DATE = LocalDate.now();

    @Inject
    FormDataService sut;

    @InjectMock
    @RestClient
    SpeedmentClientService speedmentClientService;

}
