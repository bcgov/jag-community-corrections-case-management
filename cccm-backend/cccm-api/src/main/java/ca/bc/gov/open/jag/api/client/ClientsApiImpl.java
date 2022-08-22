package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.cccm.api.openapi.ClientsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Photo;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
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

        return clientDataService.clientProfile(clientNum);

    }

    @Override
    @RolesAllowed("client-search")
    public Photo getClientPhoto(BigDecimal clientId) {

        return clientDataService.clientPhoto(clientId);

    }

    @Override
    @RolesAllowed("client-search")
    public List<Client> searchClients(String name, Boolean soundex, Integer birthYear, Integer age, String address, String location, String gender, String clientNum, String officer) {

        logger.info("Client Search Request");

        return clientDataService.clientSearch(new ClientSearch(name, soundex, birthYear, age, address, location, gender, clientNum, officer));

    }

}
