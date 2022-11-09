package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.mapper.ClientMapper;
import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.bc.gov.open.jag.api.Keys.*;
import static ca.bc.gov.open.jag.api.util.JwtUtils.stripUserName;

@RequestScoped
@Slf4j
public class ClientDataServiceImpl implements ClientDataService {

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
            searchType = SEARCH_TYPE_SOUNDEX;
        } else if (StringUtils.isNoneBlank(clientSearch.getIdentifierType())) {
            searchType = SEARCH_TYPE_ID;
        } else if (StringUtils.isNoneBlank(clientSearch.getLastName()) && clientSearch.getLastName().contains("%")) {
            searchType = SEARCH_TYPE_PARTIAL;
        } else {
            searchType = SEARCH_TYPE_EXACT;
        }
        //Validation tbd
        return createClientResult(obridgeClientService.getClientSearch(searchType, clientSearch.getLastName(),
                clientSearch.getGivenName(),
                (clientSearch.getBirthYear() != null ? BigDecimal.valueOf(clientSearch.getBirthYear()) : null),
                (clientSearch.getRange() != null ? BigDecimal.valueOf(clientSearch.getRange()): null),
                clientSearch.getAge(),
                clientSearch.getGender(),
                clientSearch.getIdentifierType(),
                clientSearch.getIdentifier(),
                stripUserName(clientSearch.getUser()),
                clientSearch.getLocationId()
        ));

    }

    @Override
    public List<Client> clientAddressSearch(ClientAddressSearch clientAddressSearch) {

        return createClientResult(obridgeClientService.getClientAddressSearch(
                clientAddressSearch.getAddressType(),
                clientAddressSearch.getAddress(),
                clientAddressSearch.getCity(),
                clientAddressSearch.getProvince(),
                clientAddressSearch.getPostalCode(),
                clientAddressSearch.getExpired(),
                stripUserName(clientAddressSearch.getUser()),
                clientAddressSearch.getLocationId()
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

        return clientMapper.toAddressList(obridgeClientService.getAddressById(csNumberPadded, stripUserName(user), new BigDecimal(location)));

    }

    @Override
    public Client clientDetails(String clientNum, String user, String location) {

        final String csNumberPadded = padCsNum(clientNum);

        log.info("Get Client Data");
        ca.bc.gov.open.jag.api.model.data.Client client = obridgeClientService.getDetailsById(csNumberPadded, stripUserName(user), new BigDecimal(location));
        log.info("Get Address Data");
        List<ca.bc.gov.open.jag.api.model.data.Address> address = obridgeClientService.getAddressById(csNumberPadded, stripUserName(user), new BigDecimal(location));
        log.info("Get Photo Data");
        Photo photo = getPhoto(csNumberPadded);

        return clientMapper.toClientDetails(client, address, photo);

    }

    private List<Client> createClientResult(List<ca.bc.gov.open.jag.api.model.data.Client> clients) {

        return clients.stream()
                .map(client1 -> clientMapper.toApiClient(client1, new ClientProfile(), null))
                .collect(Collectors.toList());

    }

    @Override
    public List<ClientFormSummary> clientFormSearch(String clientNum, boolean currentPeriod, String formTypeCd) {

        List<ClientFormSummary> forms = obridgeClientService.getClientForms(clientNum, currentPeriod, formTypeCd);
        List<ClientFormSummary> formsMerged = new ArrayList<>();

        for (ClientFormSummary form: forms) {
            Optional<ClientFormSummary> relatedFrom = getRelatedKey(forms, form.getId());

            if (formTypeCd.equalsIgnoreCase(ALL_FORM_TYPE) || formTypeCd.equalsIgnoreCase(SARA_FORM_TYPE)) {
                if (relatedFrom.isPresent() && !inListByPrimaryKey(formsMerged, form.getId()) && !inListByRelatedKey(formsMerged, form.getId()) && form.getModule().equalsIgnoreCase(CRNA_FORM_TYPE)) {

                    log.info("Merging crna and sara");

                    ClientFormSummary mergedForm = form;
                    mergedForm.setModule(MessageFormat.format("{0}-{1}", form.getModule(), relatedFrom.get().getModule()));
                    mergedForm.setStatus(relatedFrom.get().getStatus());
                    mergedForm.getRatings().putAll(relatedFrom.get().getRatings());
                    mergedForm.setLocationId(relatedFrom.get().getLocationId());
                    mergedForm.setLocation(relatedFrom.get().getLocation());
                    mergedForm.setUpdatedBy(relatedFrom.get().getUpdatedBy());
                    if (relatedFrom.get().getUpdatedDate() != null && relatedFrom.get().getUpdatedDate().isAfter(mergedForm.getUpdatedDate())) {
                        mergedForm.setUpdatedDate(relatedFrom.get().getUpdatedDate());
                    }

                    mergedForm.setSupervisionRating((ratingToInteger(relatedFrom.get().getSupervisionRating()) > ratingToInteger(form.getSupervisionRating()) ? relatedFrom.get().getSupervisionRating() : form.getSupervisionRating()));
                    mergedForm.setReassessment(relatedFrom.get().getReassessment());

                    formsMerged.add(mergedForm);
                } else if (!relatedFrom.isPresent() && formTypeCd.equalsIgnoreCase(SARA_FORM_TYPE) && form.getModule().equalsIgnoreCase(SARA_FORM_TYPE)) {

                    log.info("adding stand alone form");

                    formsMerged.add(form);

                } else if (!relatedFrom.isPresent() && formTypeCd.equalsIgnoreCase(ALL_FORM_TYPE)) {
                    formsMerged.add(form);
                }
            }
            else if (formTypeCd.equalsIgnoreCase(CRNA_FORM_TYPE) && form.getModule().equalsIgnoreCase(CRNA_FORM_TYPE) && form.getRelatedClientFormId() == null) {
                formsMerged.add(form);
            }
        }

        return formsMerged;
    }

    @Override
    public String getClientFormJSON(BigDecimal clientFormId,String clientNumber,  boolean includeValues) {
        log.debug("Getting client form JSON {} {} {}", clientFormId, clientNumber, includeValues);
        return obridgeClientService.getClientFormAsJSON(clientNumber, clientFormId, includeValues );
    }


    @Override
    public ClientFormSummary getClientFormSummary(BigDecimal clientFormId, String clientNumber) {
        return obridgeClientService.getClientFormSummary(clientNumber, clientFormId);
    }

    @Override
    public String saveClientFormAnswers(String clientNumber,BigDecimal clientFormId, String payload, boolean loadLatestValues) {
        log.debug("Saving client form answers {}", clientFormId);
        return obridgeClientService.saveClientFormAnswers(clientNumber,clientFormId, payload, loadLatestValues);

    }


    @Override
    public void deleteInterventionsExcept(String clientNumber, BigDecimal clientFormId, String payload) {
        log.debug("Updating client form interventions {} {}", clientFormId, payload);
        obridgeClientService.updateClientFormInterventions(clientNumber,clientFormId, payload);
    }


    @Override
    public String getClientFormAnswers(String clientNumber,BigDecimal clientFormId) {
        log.debug("Getting form answers {}", clientFormId);
        return obridgeClientService.getClientFormAnswers(clientNumber,clientFormId);
    }

    @Override
    public String getClientFormAnswersSummary(String clientNumber, BigDecimal clientFormId, Boolean includeLinkedForm) {
        return obridgeClientService.getClientFormAnswersSummary(clientNumber, clientFormId, includeLinkedForm);
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
    public List<Responsivity> searchClientFormResponsivities(ClientSearchInput searchInput) {
        return obridgeClientService.searchClientResponsivities(searchInput);
    }

    @Override
    public List<Intervention> searchClientFormInterventions(ClientSearchInput searchInput) {
        return obridgeClientService.searchClientInterventions( searchInput);
    }

    @Override
    public List<Comment> searchClientFormComments(ClientSearchInput searchInput) {
        return obridgeClientService.searchClientComments( searchInput);
    }

    @Override
    public String getClientFormIntervetionForCasePlan(String csNumber, BigDecimal clientFormId, boolean includeLinkedForm) {
        log.debug("Get client form intervention for case plan, clientFormId: {}, includeLinkedForm: {}", clientFormId, includeLinkedForm);
        return obridgeClientService.getClientFormInterventionsForCasePlan(csNumber, clientFormId, includeLinkedForm);
    }
    
    @Override
    public String getClientFormMetaJson(String csNumber, BigDecimal clientFormId) {
        return obridgeClientService.getClientFormMetaJson(csNumber, clientFormId);
    }
    
    @Override
    public void updateSourcesContacted(BigDecimal clientFormId, String sourcesContacted) {
        obridgeClientService.updateSourcesContacted(clientFormId, sourcesContacted);
    }
    
    @Override
    public void upcertResponsivities(BigDecimal clientFormId, String payload) {
        // TODO Auto-generated method stub
        
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

    private Boolean inListByPrimaryKey(List<ClientFormSummary> forms, BigDecimal key) {
        return forms.stream().anyMatch(clientFormSummary -> clientFormSummary.getId().equals(key));
    }

    private Boolean inListByRelatedKey(List<ClientFormSummary> forms, BigDecimal key) {
        return forms.stream().anyMatch(clientFormSummary -> clientFormSummary.getRelatedClientFormId() != null && clientFormSummary.getRelatedClientFormId().equals(key));
    }

    private Optional<ClientFormSummary> getRelatedKey(List<ClientFormSummary> forms, BigDecimal key) {
        return forms.stream().filter(clientFormSummary -> clientFormSummary.getRelatedClientFormId() != null && clientFormSummary.getRelatedClientFormId().equals(key)).findFirst();
    }

    private Integer ratingToInteger(String rating) {

        if (StringUtils.isBlank(rating)) {
            return -1;
        }

        int result;

        switch (rating) {
            case HIGH:
                result = 3;
                break;
            case MEDIUM:
                result = 2;
                break;
            case LOW:
                result = 1;
                break;
            default:
                result = 0;
                break;
        }

        return result;
    }

}
