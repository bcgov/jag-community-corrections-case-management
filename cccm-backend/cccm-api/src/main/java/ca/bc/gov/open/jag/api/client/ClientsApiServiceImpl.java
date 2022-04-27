package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.ClientsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientList;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

@ApplicationScoped
public class ClientsApiServiceImpl implements ClientsApi {

    @Override
    @Transactional
    public ClientDetails addClient(@Valid ClientDetails clientDetails) {
        //Success
        return clientDetails;

    }

    @Override
    @Transactional
    public void deleteClient(BigDecimal clientId) {
        //TODO implement when ready
    }

    @Override
    @Transactional
    public ClientDetails getClient(BigDecimal clientId) {

        return createMockClient();

    }

    @Override
    @Transactional
    public ClientList getClients() {

        //Mock
        ClientList clientList = new ClientList();
        clientList.setItems(Collections.singletonList(createMockClient()));

        return clientList;
    }

    @Override
    @Transactional
    public ClientDetails updateClient(@Valid ClientDetails clientDetails) {
        return clientDetails;
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
