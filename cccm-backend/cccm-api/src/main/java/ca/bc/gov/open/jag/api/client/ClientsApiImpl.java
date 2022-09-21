package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.ClientsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Name;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Photo;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class ClientsApiImpl implements ClientsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(ClientsApiImpl.class));

    @Inject
    ClientDataService clientDataService;

    @Inject
    @Claim(standard = Claims.preferred_username)
    String username;

    @Override
    @RolesAllowed("client-search")
    public Client getClient(String xLocationId, String clientNum) {

        logger.info(username);

        return clientDataService.clientProfile(clientNum, username, xLocationId);

    }

    @Override
    @RolesAllowed("client-search")
    public Photo getClientPhoto(String xLocationId, String clientNum) {

        return clientDataService.clientPhoto(clientNum);

    }

    @Override
    @RolesAllowed("client-search")
    public List<Client> searchClients(String xLocationId, String lastName, Boolean soundex, String givenName, Integer birthYear, Integer age, Integer range, Boolean currentLocation, String gender, String identifierType, String identifier) {

        logger.info("Client Search Request");

        return clientDataService.clientSearch(new ClientSearch(lastName, soundex, givenName, birthYear, age, range, currentLocation, gender, identifierType, identifier));

    }

    @Override
    @RolesAllowed("client-search")
    public List<Client> searchClientAddress(String xLocationId, String addressType, String address, String city, String province, String postalCode, Boolean expired, Boolean currentLocation) {

        logger.info("Client Address Search Request");

        return clientDataService.clientAddressSearch(new ClientAddressSearch(addressType, address, city, province, postalCode, expired, currentLocation));

    }

    @Override
    @RolesAllowed("client-search")
    public List<Address> getClientAddress(String clientNum, String xLocationId) {

        logger.info("Client Address Request");

        return clientDataService.clientAddress(clientNum, username, xLocationId);

    }

    @Override
    @RolesAllowed("client-search")
    public List<Name> getClientNames(String clientNum, String xLocationId) {

        logger.info("Client Names Request");

        return clientDataService.clientNames(clientNum, username, xLocationId);

    }

}
