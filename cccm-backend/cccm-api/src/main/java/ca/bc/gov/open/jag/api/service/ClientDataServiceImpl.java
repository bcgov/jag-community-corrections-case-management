package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.mapper.ClientMapper;
import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ca.bc.gov.open.jag.api.util.JwtUtils.stripUserName;

@RequestScoped
public class ClientDataServiceImpl implements ClientDataService {

    private static final Logger logger = Logger.getLogger(String.valueOf(ClientDataServiceImpl.class));

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

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
    public List<Client> clientAddressSearch(ClientAddressSearch clientAddressSearch) {

        return createClientResult(obridgeClientService.getClientAddressSearch(
                clientAddressSearch.getAddressType(),
                clientAddressSearch.getAddress(),
                clientAddressSearch.getCity(),
                clientAddressSearch.getProvince(),
                clientAddressSearch.getPostalCode(),
                clientAddressSearch.getExpired()
        ));

    }

    @Override
    public Client clientProfile(String clientNum, String user, String location) {

        final String csNumberPadded = padCsNum(clientNum);

        Photo photo = getPhoto(csNumberPadded);

        ca.bc.gov.open.jag.api.model.data.ClientProfile result = obridgeClientService.getProfileById(csNumberPadded, stripUserName(user), new BigDecimal(location));

        return clientMapper.toApiClient(result.getClient(), result, photo);

    }

    @Override
    public ca.bc.gov.open.jag.cccm.api.openapi.model.Photo clientPhoto(String clientNum) {

        final String csNumberPadded = padCsNum(clientNum);

        List<Photo> photos = obridgeClientService.getPhotosById(csNumberPadded);

        if (!photos.isEmpty()) {
            return clientMapper.toPhoto(photos.stream().findFirst().get());
        } else {
            throw new CCCMException("Photo not found", CCCMErrorCode.RECORDNOTFOUND);
        }

    }

    @Override
    public List<Address> clientAddress(String clientNum, String user, String location) {

        final String csNumberPadded = padCsNum(clientNum);

        return clientMapper.toAddressList(obridgeClientService.getAddressById(csNumberPadded, user, new BigDecimal(location)));

    }

    @Override
    public Client clientDetails(String clientNum, String user, String location) {

        final String csNumberPadded = padCsNum(clientNum);

        logger.info("Get Client Data");
        ca.bc.gov.open.jag.api.model.data.Client client = obridgeClientService.getDetailsById(csNumberPadded, user, new BigDecimal(location));
        logger.info("Get Address Data");
        List<ca.bc.gov.open.jag.api.model.data.Address> address = obridgeClientService.getAddressById(csNumberPadded, user, new BigDecimal(location));
        logger.info("Get Photo Data");
        Photo photo = getPhoto(csNumberPadded);

        return clientMapper.toClientDetails(client, address, photo);

    }

    private List<Client> createClientResult(List<ca.bc.gov.open.jag.api.model.data.Client> clients) {

        return clients.stream()
                .map(client1 -> clientMapper.toApiClient(client1, new ClientProfile(), null))
                .collect(Collectors.toList());

    }

    private Photo getPhoto(String clientNum) {

        List<Photo> photos = obridgeClientService.getPhotosById(clientNum);

        if (!photos.isEmpty()) {
            return photos.stream().findFirst().get();
        }

        return null;

    }

    private String padCsNum(String clientNum) {
        return ("00000000" + clientNum).substring(clientNum.length());
    }

}
