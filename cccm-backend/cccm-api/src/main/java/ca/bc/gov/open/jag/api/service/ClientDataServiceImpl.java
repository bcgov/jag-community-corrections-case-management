package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.mapper.ClientMapper;
import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ClientDataServiceImpl implements ClientDataService {

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Inject
    ClientMapper clientMapper;

    @Override
    public List<Client> clientSearch(ClientSearch clientSearch) {

        //TODO: add additional search fields when present

        return createClientResult(obridgeClientService.getClientSearch("EXACT", clientSearch.getName()));
    }

    @Override
    public Client clientProfile(String clientNum) {

        final String csNumberPadded = ("00000000" + clientNum).substring(clientNum.length());

        BigDecimal  dbClientId = speedmentClientService.getClientId(csNumberPadded);

        Optional<ca.bc.gov.open.jag.api.model.data.Client> result = obridgeClientService.getClientById("ID", "CSNO", csNumberPadded).stream().findFirst();

        if (result.isPresent()) {

            return clientMapper.toApiClient(result.get(), obridgeClientService.getProfileById(dbClientId), speedmentClientService.getClientAddress(csNumberPadded), speedmentClientService.getAlerts(dbClientId), dbClientId);

        } else {

            throw new CCCMException("Client not found", CCCMErrorCode.RECORDNOTFOUND);

        }

    }

    @Override
    public byte[] clientPhoto(BigDecimal clientId) {

        List<Photo> photos = obridgeClientService.getPhotosById(clientId);

        if (!photos.isEmpty()) {
            return photos.stream().findFirst().get().getImage();
        } else {
            throw new CCCMException("Photo not found", CCCMErrorCode.RECORDNOTFOUND);
        }

    }

    private List<Client> createClientResult(List<ca.bc.gov.open.jag.api.model.data.Client> clients) {

        return clients.stream()
                .map(client1 -> clientMapper.toApiClient(client1, new ClientProfile(), speedmentClientService.getClientAddress(client1.getClientNo()), Collections.emptyList(), null))
                .collect(Collectors.toList());

    }

}
