package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.mapper.ClientMapper;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.ClientsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class ClientsApiImpl implements ClientsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(ClientsApiImpl.class));

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Inject
    ClientMapper clientMapper;

    @Override
    @RolesAllowed("client-search")
    public File getClientPhoto(BigDecimal clientId) {
        return null;
    }

    @Override
    @RolesAllowed("client-search")
    public List<Client> searchClients(String name, Boolean soundex, Integer birthYear, Integer age, String address, String location, String gender, BigDecimal clientId) {

        logger.info("Client Search Request");

        return createClientResult(obridgeClientService.getClientSearch("EXACT", name));

    }

    private List<Client> createClientResult(List<ca.bc.gov.open.jag.api.model.Client> clients) {

        return clients.stream()
                .map(client1 -> clientMapper.toApiClient(client1, speedmentClientService.getClientAddress(client1.getClientNo().toPlainString())))
                .collect(Collectors.toList());

    }

}
