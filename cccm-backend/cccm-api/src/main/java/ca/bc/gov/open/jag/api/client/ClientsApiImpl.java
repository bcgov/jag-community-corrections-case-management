package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.mapper.ClientMapper;
import ca.bc.gov.open.jag.api.model.ClientProfile;
import ca.bc.gov.open.jag.api.model.Photo;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.ClientsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    public Client getClient(String clientNum) {

        final String csNumberPadded = ("00000000" + clientNum).substring(clientNum.length());

        BigDecimal  dbClientId = speedmentClientService.getClientId(csNumberPadded);

        Optional<ca.bc.gov.open.jag.api.model.Client> result = obridgeClientService.getClientById("ID", "CSNO", csNumberPadded).stream().findFirst();

        if (result.isPresent()) {

            return clientMapper.toApiClient(result.get(), obridgeClientService.getProfileById(dbClientId), speedmentClientService.getClientAddress(csNumberPadded), speedmentClientService.getAlerts(dbClientId), dbClientId);

        } else {

            throw new CCCMException("Client not found", CCCMErrorCode.RECORDNOTFOUND);

        }

    }

    @Override
    @RolesAllowed("client-search")
    public byte[] getClientPhoto(BigDecimal clientId) {

        List<Photo> photos = obridgeClientService.getPhotosById(clientId);

        if (!photos.isEmpty()) {
            return photos.stream().findFirst().get().getImage();
        } else {
            throw new CCCMException("Photo not found", CCCMErrorCode.RECORDNOTFOUND);
        }

    }

    @Override
    @RolesAllowed("client-search")
    public List<Client> searchClients(String name, Boolean soundex, Integer birthYear, Integer age, String address, String location, String gender, String clientNum) {

        logger.info("Client Search Request");

        return createClientResult(obridgeClientService.getClientSearch("EXACT", name));

    }

    private List<Client> createClientResult(List<ca.bc.gov.open.jag.api.model.Client> clients) {

        return clients.stream()
                .map(client1 -> clientMapper.toApiClient(client1, new ClientProfile(), speedmentClientService.getClientAddress(client1.getClientNo()), Collections.emptyList(), null))
                .collect(Collectors.toList());

    }

}
