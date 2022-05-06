package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.ClientsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientList;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.logging.Logger;

@ApplicationScoped
public class ClientsApiServiceImpl implements ClientsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(ClientsApiServiceImpl.class));

    @Override
    @Transactional
    public ClientDetails getClient(BigDecimal clientId) {

        logger.info("Get client request received");

        return createMockClient();

    }

    @Override
    @Transactional
    public ClientList getClients() {

        logger.info("Get clients request received");

        //Mock
        ClientList clientList = new ClientList();
        clientList.setItems(Collections.singletonList(createMockClient()));

        return clientList;
    }

    /**
     * This for mocking data until we get the database
     * @return new mock data client
     */
    private ClientDetails createMockClient() {

        //Mock
        ClientDetails clientDetails = new ClientDetails();
        clientDetails.setClientId(BigDecimal.ONE);
        clientDetails.setFirstName("John");
        clientDetails.setLastName("Smith");
        clientDetails.setGender("M");
        clientDetails.setLocation("Vancouver, BC");
        clientDetails.setBirthDate(LocalDate.of(1982, 3, 4));
        clientDetails.setFinalOrderExpiryDate(LocalDate.of(2021, 10, 2));

        return clientDetails;

    }


}
