package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.ClientsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Photo;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@RequestScoped
public class ClientsApiImpl implements ClientsApi {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(ClientsApiImpl.class));

    @Inject
    ClientDataService clientDataService;

    @Inject
    @Claim(standard = Claims.preferred_username)
    String username;

    @Override
    @RolesAllowed("client-search")
    public Client getClient(String clientNum, String xLocationId) {

        logger.info("Client Profile Request");

        return clientDataService.clientProfile(clientNum, username, xLocationId);

    }

    @Override
    @RolesAllowed("client-search")
    public Photo getClientPhoto(String clientNum, String xLocationId) {

        logger.info("Client Photo Request");

        return clientDataService.clientPhoto(clientNum, xLocationId);

    }

    @Override
    @RolesAllowed("client-search")
    public List<Client> searchClients(String xLocationId, String lastName, Boolean soundex, String givenName, Integer birthYear, Integer age, Integer range, Boolean currentLocation, String gender, String identifierType, String identifier) {

        logger.info("Client Search Request");

        return clientDataService.clientSearch(new ClientSearch(lastName, soundex, givenName, birthYear, age, range, currentLocation, gender, identifierType, identifier, username, new BigDecimal(xLocationId)));

    }

    @Override
    @RolesAllowed("client-search")
    public List<Client> searchClientAddress(String xLocationId, String addressType, String address, String city, String province, String postalCode, Boolean expired, Boolean currentLocation) {

        logger.info("Client Address Search Request");

        return clientDataService.clientAddressSearch(new ClientAddressSearch(addressType, address, city, province, postalCode, expired, currentLocation, username, new BigDecimal(xLocationId)));

    }

    @Override
    @RolesAllowed("client-search")
    public List<Address> getClientAddress(String clientNum, String xLocationId) {

        logger.info("Client Address Request");

        return clientDataService.clientAddress(clientNum, username, xLocationId);

    }

    @Override
    @RolesAllowed("client-search")
    public Client getClientDetails(String clientNum, String xLocationId) {

        logger.info("Client Names Request");

        return clientDataService.clientDetails(clientNum, username, xLocationId);

    }

}
