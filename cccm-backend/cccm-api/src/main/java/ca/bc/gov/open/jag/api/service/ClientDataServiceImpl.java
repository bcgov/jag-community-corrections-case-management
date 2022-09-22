package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.mapper.ClientMapper;
import ca.bc.gov.open.jag.api.mapper.FormMapper;
import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ca.bc.gov.open.jag.api.util.JwtUtils.stripUserName;

@ApplicationScoped
@Slf4j
public class ClientDataServiceImpl implements ClientDataService {

    public static final String LOCATION = "location";

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Inject
    ClientMapper clientMapper;

    @Inject
    FormMapper formMapper;

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
    public Client clientProfile(String clientNum, String user) {

        final String csNumberPadded = ("00000000" + clientNum).substring(clientNum.length());

        BigDecimal  dbClientId = speedmentClientService.getClientId(csNumberPadded);

        Map location = obridgeClientService.getLocation();

        ca.bc.gov.open.jag.api.model.data.ClientProfile result = obridgeClientService.getProfileById(csNumberPadded, stripUserName(user), BigDecimal.valueOf((Double) location.get("locationId")));

        return clientMapper.toApiClient(result.getClient(), result, dbClientId);

    }

    @Override
    public ca.bc.gov.open.jag.cccm.api.openapi.model.Photo clientPhoto(String clientNum) {

        List<Photo> photos = obridgeClientService.getPhotosById(clientNum);

        if (!photos.isEmpty()) {
            return clientMapper.toPhoto("", photos.stream().findFirst().get().getImage());
        } else {
            throw new CCCMException("Photo not found", CCCMErrorCode.RECORDNOTFOUND);
        }

    }

    private List<Client> createClientResult(List<ca.bc.gov.open.jag.api.model.data.Client> clients) {

        return clients.stream()
                .map(client1 -> clientMapper.toApiClient(client1, new ClientProfile(), null))
                .collect(Collectors.toList());

    }

    @Override
    public List<ClientFormSummary> clientFormSearch(String clientNum, boolean currentPeriod, String formTypeCd) {
        return obridgeClientService.getClientForms(clientNum, currentPeriod, formTypeCd);
    }

    @Override
    public BigDecimal addClientForm(CreateFormInput createFormInput) {

        // todo - how do we get the userid and location id?
        createFormInput.setCreatedByUserId(BigDecimal.TEN);
        createFormInput.setLocationId(BigDecimal.TEN);
        return obridgeClientService.createForm(createFormInput);
    }

    @Override
    public String getClientFormJSON(BigDecimal clientFormId,String clientNumber,  boolean includeValues) {
        log.debug("Getting client form JSON {} {} {}", clientFormId, clientNumber, includeValues);
        return obridgeClientService.getClientFormAsJSON(clientNumber, clientFormId, includeValues );
    }

    @Override
    public String saveClientFormAnswers(String clientNumber,BigDecimal clientFormId, String payload, boolean loadLatestValues) {
        log.debug("Saving client form answers {}", clientFormId);
        return obridgeClientService.saveClientFormAnswers(clientNumber,clientFormId, payload, loadLatestValues);

    }

    @Override
    public String getClientFormAnswers(String clientNumber,BigDecimal clientFormId) {
        log.debug("Getting form answers {}", clientFormId);
        return obridgeClientService.getClientFormAnswers(clientNumber,clientFormId);
    }

    @Override
    public String getClientFormAnswersForSection(String clientNumber, BigDecimal clientFormId, int sectionSequence) {
        return obridgeClientService.getClientFormAnswersForSection(clientNumber,clientFormId, sectionSequence);
    }

    @Override
    public String getClientFormAnswersForSectionAndQuestion(String clientNumber, BigDecimal clientFormId, int sectionSequence, int questionSequence) {
        return obridgeClientService.getClientFormAnswersForSectionAndQuestion(clientNumber,clientFormId, sectionSequence, questionSequence);
    }

    @Override
    public List<LabelValuePair> getClientFormFactors(String reportType, String csNumber) {
        return obridgeClientService.getClientFormFactors(reportType, csNumber);
    }

    @Override
    public ChartDataSet getClientChartData(String reportType, String csNumber) {
        return obridgeClientService.getClientChartData(reportType, csNumber);
    }

    @Override
    public List<Responsivity> getClientFormResponsivities(String csNumber, ClientSearchInput searchInput) {
        return obridgeClientService.searchClientResponsivities(csNumber,searchInput);
    }

    @Override
    public List<Intervention> getClientFormInterventions(String csNumber, ClientSearchInput searchInput) {
        return obridgeClientService.searchClientInterventions(csNumber, searchInput);
    }

    @Override
    public List<Comment> getClientFormComments(String csNumber, ClientSearchInput searchInput) {
        return obridgeClientService.searchClientComments(csNumber, searchInput);
    }
}
