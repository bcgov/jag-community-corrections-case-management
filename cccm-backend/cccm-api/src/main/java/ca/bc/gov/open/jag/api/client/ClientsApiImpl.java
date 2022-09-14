package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.model.Form;
import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.ClientsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSearchList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Photo;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ClientsApiImpl implements ClientsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(ClientsApiImpl.class));

    @Inject
    ClientDataService clientDataService;

    @Inject
    @Claim(standard = Claims.preferred_username)
    String username;

    @Override
    @RolesAllowed("client-search")
    public Client getClient(String clientNum) {

        logger.info(username);

        return clientDataService.clientProfile(clientNum, username);

    }

    @Override
    @RolesAllowed("client-search")
    public Photo getClientPhoto(String clientNum) {

        return clientDataService.clientPhoto(clientNum);

    }

    @Override
    @RolesAllowed("client-search")
    public List<Client> searchClients(String lastName, Boolean soundex, String givenName, Integer birthYear, Integer age, Integer range, String location, String gender, String identifierType, String identifier, String officer) {

        logger.info("Client Search Request");

        return clientDataService.clientSearch(new ClientSearch(lastName, soundex, givenName, birthYear, age, range, location, gender, identifierType, identifier, officer));

    }

    @Override
    @RolesAllowed("client-search")
    public List<Client> searchClientAddress(String addressType, String address, String city, String province, String postalCode, Boolean expired) {

        logger.info("Client Address Search Request");

        return clientDataService.clientAddressSearch(new ClientAddressSearch(addressType, address, city, province, postalCode, expired));

    }
}
