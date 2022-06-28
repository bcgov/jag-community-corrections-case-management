package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.ClientsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ClientsApiImpl implements ClientsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(ClientsApiImpl.class));

    @Override
    @RolesAllowed("client-search")
    public List<Client> searchClients(String name, Boolean soundex, Integer birthYear, Integer age, String address, String location, String gender, BigDecimal clientId) {

        logger.info("Client Search Request");

        return createClientResult();

    }

    private List<Client> createClientResult() {

        Client client = new Client();
        client.setBirthDate("");
        client.setClientAge(BigDecimal.TEN);
        client.setFirstName("Bob");
        client.setLastName("Ross");
        client.setGender("Male");

        Address address = new Address();
        address.setCity("Victoria");
        address.setPostalCode("123 abc");
        address.setStreet("123 Hello St");
        client.setAddress(Collections.singletonList(address));

        return Collections.singletonList(client);

    }

}
