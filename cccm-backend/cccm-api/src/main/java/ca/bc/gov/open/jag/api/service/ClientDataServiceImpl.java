package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.mapper.ClientMapper;
import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ca.bc.gov.open.jag.api.util.JwtUtils.stripUserName;

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

        String searchType;
        //This is based on the current stored procedure
        if (Boolean.TRUE.equals(clientSearch.getSoundex())) {
            searchType = "SOUNDEX";
        } else if (StringUtils.isNoneBlank(clientSearch.getIdentifierType())) {
            searchType = "ID";
        } else if (StringUtils.isNoneBlank(clientSearch.getLastName()) && clientSearch.getLastName().contains("%")) {
            searchType = "PARTIAL";
        } else {
            searchType = "EXACT";
        }

        //Validation tbd
        return createClientResult(obridgeClientService.getClientSearch(searchType, clientSearch.getLastName(),
                clientSearch.getGivenName(),
                (clientSearch.getBirthYear() != null ? BigDecimal.valueOf(clientSearch.getBirthYear()) : null),
                (clientSearch.getRange() != null ? BigDecimal.valueOf(clientSearch.getRange()): null),
                clientSearch.getAge(),
                clientSearch.getGender(), clientSearch.getIdentifierType(),
                clientSearch.getIdentifier()));
    }

    @Override
    public Client clientProfile(String clientNum, String user) {

        final String csNumberPadded = ("00000000" + clientNum).substring(clientNum.length());

        BigDecimal  dbClientId = speedmentClientService.getClientId(csNumberPadded);

        Map location = obridgeClientService.getLocation();

        ca.bc.gov.open.jag.api.model.data.ClientProfile result = obridgeClientService.getProfileById(csNumberPadded, stripUserName(user), BigDecimal.valueOf((Double) location.get("locationId")));

        return clientMapper.toApiClient(result.getClient(), result, speedmentClientService.getAlerts(dbClientId), dbClientId);

    }

    @Override
    public ca.bc.gov.open.jag.cccm.api.openapi.model.Photo clientPhoto(BigDecimal clientId) {

        List<Photo> photos = obridgeClientService.getPhotosById(clientId);

        if (!photos.isEmpty()) {
            return clientMapper.toPhoto("", photos.stream().findFirst().get().getImage());
        } else {
            throw new CCCMException("Photo not found", CCCMErrorCode.RECORDNOTFOUND);
        }

    }

    private List<Client> createClientResult(List<ca.bc.gov.open.jag.api.model.data.Client> clients) {

        return clients.stream()
                .map(client1 -> clientMapper.toApiClient(client1, new ClientProfile(), Collections.emptyList(), null))
                .collect(Collectors.toList());

    }

}
