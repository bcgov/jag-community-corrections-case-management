package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.mapper.ClientMapper;
import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.api.util.FormUtils;
import ca.bc.gov.open.jag.api.util.MappingUtils;
import ca.bc.gov.open.jag.api.util.UpdateDateComparator;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonString;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static ca.bc.gov.open.jag.api.Keys.*;
import static ca.bc.gov.open.jag.api.util.FormUtils.ratingIntToText;
import static ca.bc.gov.open.jag.api.util.FormUtils.ratingToInteger;
import static ca.bc.gov.open.jag.api.util.JwtUtils.stripUserName;

@RequestScoped
public class ClientDataServiceImpl implements ClientDataService {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(ClientDataServiceImpl.class));

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    ClientMapper clientMapper;

    @Inject
    JsonWebToken jwt;

    @Override
    public List<Client> clientSearch(ClientSearch clientSearch) {

        logger.debug("Client Search {}", clientSearch);

        String searchType;
        //This is based on the current stored procedure
        if (Boolean.TRUE.equals(clientSearch.getSoundex())) {
            logger.info("Searching by SOUNDEX");
            searchType = SEARCH_TYPE_SOUNDEX;
        } else if (StringUtils.isNoneBlank(clientSearch.getIdentifierType())) {
            logger.info("Searching by ID");
            searchType = SEARCH_TYPE_ID;
        } else if (StringUtils.isNoneBlank(clientSearch.getLastName()) && clientSearch.getLastName().contains("%")) {
            logger.info("Searching by Partial");
            searchType = SEARCH_TYPE_PARTIAL;
        } else {
            logger.info("Searching by Exact");
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

        logger.debug("Client Address Search {}", clientAddressSearch);

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

        logger.debug("Client {} Profile for {} in location {}", clientNum, user, location);

        final String csNumberPadded = padCsNum(clientNum);

        logger.info("Getting Photo for Profile");
        Photo photo = getPhoto(csNumberPadded, new BigDecimal(location));

        logger.info("Getting Profile");
        ca.bc.gov.open.jag.api.model.data.ClientProfile result = obridgeClientService.getProfileById(csNumberPadded, stripUserName(user), new BigDecimal(location));

        logger.info("Mapping to Client");
        return clientMapper.toApiClient(result.getClient(), result, photo);

    }

    @Override
    public ca.bc.gov.open.jag.cccm.api.openapi.model.Photo clientPhoto(String clientNum, String location) {

        logger.debug("Client Photo for {}", clientNum);

        final String csNumberPadded = padCsNum(clientNum);

        List<Photo> photos = obridgeClientService.getPhotosById(csNumberPadded, new BigDecimal(location));

        if (!photos.isEmpty()) {
            return clientMapper.toPhoto(photos.stream().findFirst().get());
        } else {
            throw new CCCMException("Photo not found", CCCMErrorCode.RECORDNOTFOUND);
        }

    }

    @Override
    public List<Address> clientAddress(String clientNum, String user, String location) {

        logger.debug("Client {} Address for {} in location {}", clientNum, user, location);

        final String csNumberPadded = padCsNum(clientNum);

        return clientMapper.toAddressList(obridgeClientService.getAddressById(csNumberPadded, stripUserName(user), new BigDecimal(location)));

    }

    @Override
    public Client clientDetails(String clientNum, String user, String location) {

        logger.debug("Client {} Details for {} in location {}", clientNum, user, location);

        final String csNumberPadded = padCsNum(clientNum);

        logger.info("Get Client Data");
        ca.bc.gov.open.jag.api.model.data.Client client = obridgeClientService.getDetailsById(csNumberPadded, stripUserName(user), new BigDecimal(location));
        logger.info("Get Address Data");
        List<ca.bc.gov.open.jag.api.model.data.Address> address = obridgeClientService.getAddressById(csNumberPadded, stripUserName(user), new BigDecimal(location));
        logger.info("Get Photo Data");
        Photo photo = getPhoto(csNumberPadded, new BigDecimal(location));

        return clientMapper.toClientDetails(client, address, photo);

    }

    private List<Client> createClientResult(List<ca.bc.gov.open.jag.api.model.data.Client> clients) {

        return clients.stream()
                .map(client1 -> clientMapper.toApiClient(client1, new ClientProfile(), null))
                .collect(Collectors.toList());

    }

    @Override
    public List<ClientFormSummary> clientFormSearch(String clientNum, boolean currentPeriod, boolean mostRecent, String formTypeCd, String location) {

        logger.debug("Client Form Search Client {} current period {} formTypeCd {}", clientNum, currentPeriod, formTypeCd);

        logger.info("Getting Forms");
        List<ClientFormSummary> forms = obridgeClientService.getClientForms(clientNum, currentPeriod, formTypeCd, new BigDecimal(location));
        List<ClientFormSummary> formsMerged = new ArrayList<>();
        boolean hasSMOEarlyAdopter = hasSMOEarlyAdopter();

        for (ClientFormSummary form: forms) {
            //When searching for only most recent skip records that are false
            if (mostRecent && (form.getMostRecent() == null || !form.getMostRecent())) continue;

            Optional<ClientFormSummary> relatedFrom = getRelatedKey(forms, form.getId());
            //Set form locked for all forms
            logger.info("Calculate Locked");
            form.setLocked(MappingUtils.calculateLocked(form.getCreatedDate()));

            if (formTypeCd.equalsIgnoreCase(ALL_FORM_TYPE) || formTypeCd.equalsIgnoreCase(SARA_FORM_TYPE)) {
                if (relatedFrom.isPresent() && !inListByPrimaryKey(formsMerged, form.getId()) && !inListByRelatedKey(formsMerged, form.getId()) && form.getModule().equalsIgnoreCase(CRNA_FORM_TYPE)) {

                    logger.info("Merging crna and sara");

                    ClientFormSummary mergedForm = form;
                    mergedForm.setModule(MessageFormat.format("{0}-{1}", form.getModule(), relatedFrom.get().getModule()));
                    mergedForm.setStatus(relatedFrom.get().getStatus());
                    mergedForm.getRatings().addAll(relatedFrom.get().getRatings());
                    mergedForm.setLocationId(relatedFrom.get().getLocationId());
                    mergedForm.setLocation(relatedFrom.get().getLocation());
                    mergedForm.setUpdatedBy(relatedFrom.get().getUpdatedBy());
                    if (relatedFrom.get().getOsuUpdateDate() != null && mergedForm.getOsuUpdateDate() != null && relatedFrom.get().getOsuUpdateDate().isAfter(mergedForm.getOsuUpdateDate())) {
                        mergedForm.setOsuUpdateDate(relatedFrom.get().getOsuUpdateDate());
                    }
                    if (!relatedFrom.get().getRatings().isEmpty() && !form.getRatings().isEmpty()) {
                        mergedForm.setSupervisionRating((ratingToInteger(relatedFrom.get().getRatings().get(0).getText()) > ratingToInteger(form.getRatings().get(0).getText()) ? relatedFrom.get().getRatings().get(0).getText() : form.getRatings().get(0).getText()));
                    } else if (!relatedFrom.get().getRatings().isEmpty()) {
                        mergedForm.setSupervisionRating(relatedFrom.get().getRatings().get(0).getText());
                    } else if (!form.getRatings().isEmpty()) {
                        mergedForm.setSupervisionRating(form.getRatings().get(0).getText());
                    }
                    mergedForm.setReassessment(relatedFrom.get().getReassessment());

                    formsMerged.add(mergedForm);
                } else if (!relatedFrom.isPresent() && formTypeCd.equalsIgnoreCase(SARA_FORM_TYPE) && form.getModule().equalsIgnoreCase(SARA_FORM_TYPE)) {

                    logger.info("adding stand alone form");
                    if (!form.getRatings().isEmpty()) {
                        form.setSupervisionRating(form.getRatings().get(0).getText());
                    }
                    formsMerged.add(form);

                } else if ((!relatedFrom.isPresent() && formTypeCd.equalsIgnoreCase(ALL_FORM_TYPE)) ||
                        (formTypeCd.equalsIgnoreCase(ALL_FORM_TYPE) && (form.getModule().equalsIgnoreCase(ACUTE_FORM_TYPE) || form.getModule().equalsIgnoreCase(STATIC99R_FORM_TYPE)))) {
                    logger.info("adding other forms");
                    if (hasSMOEarlyAdopter || form.getModule().equalsIgnoreCase(CRNA_FORM_TYPE) || form.getModule().equalsIgnoreCase(SARA_FORM_TYPE)) {
                        if (!form.getRatings().isEmpty()) {
                            form.setSupervisionRating(form.getRatings().get(0).getText());
                        }
                        if (form.getModule().equalsIgnoreCase(OVERALL_FORM_TYPE)) {
                            formsMerged.add(populateRatings(form, clientNum, location));
                        } else {
                            formsMerged.add(form);
                        }

                    }
                }
            } else if ((formTypeCd.equalsIgnoreCase(CRNA_FORM_TYPE) && form.getModule().equalsIgnoreCase(CRNA_FORM_TYPE) && form.getRelatedClientFormId() == null) ||
                    (formTypeCd.equalsIgnoreCase(ACUTE_FORM_TYPE) && form.getModule().equalsIgnoreCase(ACUTE_FORM_TYPE) && hasSMOEarlyAdopter) ||
                    (formTypeCd.equalsIgnoreCase(STATIC99R_FORM_TYPE) && form.getModule().equalsIgnoreCase(STATIC99R_FORM_TYPE) && hasSMOEarlyAdopter) ||
                    (formTypeCd.equalsIgnoreCase(STABLE_FORM_TYPE) && form.getModule().equalsIgnoreCase(STABLE_FORM_TYPE) && hasSMOEarlyAdopter)) {
                logger.info("adding form {}", form.getModule());
                if (!form.getRatings().isEmpty()) {
                    form.setSupervisionRating(form.getRatings().get(0).getText());
                }
                formsMerged.add(form);
            } else if ((formTypeCd.equalsIgnoreCase(OVERALL_FORM_TYPE) && form.getModule().equalsIgnoreCase(OVERALL_FORM_TYPE)) && hasSMOEarlyAdopter) {
                logger.info("adding form {}", form.getModule());

                formsMerged.add(populateRatings(form, clientNum, location));
            }
        }
        //Add sorting on update date when implemented
        formsMerged.sort(new UpdateDateComparator());
        return formsMerged;
    }

    @Override
    public String getClientFormJSON(BigDecimal clientFormId,String clientNumber,  boolean includeValues, String location) {
        logger.debug("Getting client form JSON {} {} {}", clientFormId, clientNumber, includeValues);
        return obridgeClientService.getClientFormAsJSON(clientNumber, clientFormId, includeValues, new BigDecimal(location));
    }


    @Override
    public ClientFormSummary getClientFormSummary(BigDecimal clientFormId, String clientNumber, String location) {

        logger.debug("Client Form Summary for Form {} client {}", clientFormId, clientNumber);

        ClientFormSummary clientFormSummary = obridgeClientService.getClientFormSummary(clientNumber, clientFormId, new BigDecimal(location));
        //Apply locked form logic
        clientFormSummary.setLocked(MappingUtils.calculateLocked(clientFormSummary.getCreatedDate()));

        return clientFormSummary;
    }

    @Override
    public String saveClientFormAnswers(String clientNumber,BigDecimal clientFormId, String payload, boolean loadLatestValues, String location) {
        logger.debug("Saving client form answers {}", clientFormId);
        return obridgeClientService.saveClientFormAnswers(clientNumber,clientFormId, payload, loadLatestValues, new BigDecimal(location));

    }


    @Override
    public void deleteInterventionsExcept(String clientNumber, BigDecimal clientFormId, String payload) {
        logger.debug("Updating client form interventions {} {}", clientFormId, payload);
        obridgeClientService.updateClientFormInterventions(clientNumber,clientFormId, payload);
    }


    @Override
    public String getClientFormAnswers(String clientNumber,BigDecimal clientFormId, String location) {
        logger.debug("Getting form answers {}", clientFormId);
        return obridgeClientService.getClientFormAnswers(clientNumber,clientFormId, new BigDecimal(location));
    }

    @Override
    public String getClientFormAnswersSummary(String clientNumber, BigDecimal clientFormId, Boolean includeLinkedForm, String location) {

        logger.debug("Client Form Summary Answers for Form {} client {} include linked {}", clientFormId, clientNumber, includeLinkedForm);

        return obridgeClientService.getClientFormAnswersSummary(clientNumber, clientFormId, includeLinkedForm, new BigDecimal(location));

    }

    @Override
    public String getClientFormAnswersForSection(String clientNumber, BigDecimal clientFormId, int sectionSequence) {

        logger.debug("Client Form Summary Answers for Section {} client {} section sequence {}", clientFormId, clientNumber, sectionSequence);

        return obridgeClientService.getClientFormAnswersForSection(clientNumber,clientFormId, sectionSequence);

    }

    @Override
    public String getClientFormAnswersForSectionAndQuestion(String clientNumber, BigDecimal clientFormId, int sectionSequence, int questionSequence) {

        logger.debug("Client Form Summary Answers for Section {} client {} section sequence {} question sequence {}", clientFormId, clientNumber, sectionSequence, questionSequence);

        return obridgeClientService.getClientFormAnswersForSectionAndQuestion(clientNumber,clientFormId, sectionSequence, questionSequence);

    }

    @Override
    public List<LabelValuePair> getClientFormFactors(String reportType, String csNumber) {

        logger.debug("Client Form Factors report type {} client {}", reportType, csNumber);

        return obridgeClientService.getClientFormFactors(reportType, csNumber);

    }


    @Override
    public List<Responsivity> searchClientFormResponsivities(ClientSearchInput searchInput) {

        logger.debug("Client Form Responsivities {}", searchInput);

        return obridgeClientService.searchClientResponsivities(searchInput);

    }

    @Override
    public List<Intervention> searchClientFormInterventions(ClientSearchInput searchInput) {

        logger.debug("Client Form Interventions {}", searchInput);

        return obridgeClientService.searchClientInterventions( searchInput);

    }

    @Override
    public List<Comment> searchClientFormComments(ClientSearchInput searchInput) {

        logger.debug("Client Form Comments {}", searchInput);

        return obridgeClientService.searchClientComments( searchInput);

    }

    @Override
    public String getClientFormIntervetionForCasePlan(String csNumber, BigDecimal clientFormId, boolean includeLinkedForm, String location) {

        logger.debug("Get client form intervention for case plan, clientFormId: {}, includeLinkedForm: {}", clientFormId, includeLinkedForm);

        return obridgeClientService.getClientFormInterventionsForCasePlan(csNumber, clientFormId, includeLinkedForm, new BigDecimal(location));

    }
    
    @Override
    public String getClientFormMetaJson(String csNumber, BigDecimal clientFormId, String location) {

        logger.debug("Client Form Meta for form {} client {}", clientFormId, csNumber);

        return obridgeClientService.getClientFormMetaJson(csNumber, clientFormId, new BigDecimal(location));

    }
    
    @Override
    public void updateSourcesContacted(BigDecimal clientFormId, String sourcesContacted) {

        logger.debug("Update Sources Contacted for form {} sources {}", clientFormId, sourcesContacted);

        obridgeClientService.updateSourcesContacted(clientFormId, sourcesContacted);

    }
    
    @Override
    public void upcertResponsivities(BigDecimal clientFormId, String payload) {

        logger.warn("upcertResponsivities IS NOT IMPLEMENTED");

        // TODO Auto-generated method stub
        
    }
    
    private Photo getPhoto(String clientNum, BigDecimal location) {

        logger.debug("Get Photo for {}", clientNum);

        List<Photo> photos = obridgeClientService.getPhotosById(clientNum, location);

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

    private Boolean hasSMOEarlyAdopter() {

        if (jwt == null || jwt.claim(JWT_REALM_ACCESS).isEmpty()) return false;

        JsonObject realmAccess = (JsonObject)jwt.claim(JWT_REALM_ACCESS).get();

        List<String> roles = realmAccess.getJsonArray(JWT_REALM_ROLES)
                .stream()
                .map(value -> ((JsonString)value).getString())
                .collect(Collectors.toList());

        return (roles.contains(JWT_SMO_EARLY_ADOPTER_ROLE));

    }

    /**
     * Populate the Ratings
     * @param form
     * @param clientNumber
     * @return the form
     * NOTE this can be done in a void but is clearer with a return
     */
    private ClientFormSummary populateRatings(ClientFormSummary form, String clientNumber, String location) {
        //Get Answers From CRNA/SARA
        try {
            String answers = getClientFormAnswers(clientNumber, form.getId(), location);

            String crnaRatingAnswer = FormUtils.findAnswerByKey(answers, OVERALL_CRNA_RATING);
            String crnaRatingAnswerDesc = FormUtils.findAnswerByKey(answers, OVERALL_CRNA_RATING_DESCRIPTION);
            String saraRatingAnswer = FormUtils.findAnswerByKey(answers, OVERALL_SARA_RATING);
            String saraRatingAnswerDesc = FormUtils.findAnswerByKey(answers, OVERALL_SARA_RATING_DESCRIPTION);
            String smoOverallRatingAnswer = FormUtils.findAnswerByKey(answers, SMO_OVERALL_CRNA_RATING);
            String smoOverallRatingAnswerDesc = FormUtils.findAnswerByKey(answers, SMO_OVERALL_CRNA_RATING_DESCRIPTION);

            Rating crnaRating = new Rating();
            crnaRating.setFormType(CRNA_FORM_TYPE);
            crnaRating.setText(crnaRatingAnswer);
            crnaRating.setDesc(crnaRatingAnswerDesc);
            form.getRatings().add(crnaRating);

            Rating saraRating = new Rating();
            saraRating.setFormType(SARA_FORM_TYPE);
            saraRating.setText(saraRatingAnswer);
            saraRating.setDesc(saraRatingAnswerDesc);
            form.getRatings().add(saraRating);

            Rating smoOverallRating = new Rating();
            smoOverallRating.setFormType(OVERALL_FORM_TYPE);
            smoOverallRating.setText(smoOverallRatingAnswer);
            smoOverallRating.setDesc(smoOverallRatingAnswerDesc);
            form.getRatings().add(smoOverallRating);

            form.setSupervisionRating(getHighestRating(form.getRatings()));


        } catch (Exception e) {
            // Don't fail to return form but log the issue
            logger.error("Error mapping ratings: ", e);
        }

        return form;

    }

    private String getHighestRating(List<Rating> ratings) {
        String highestRating = "";
        Integer highestRatingInt = -1;
        for (Rating rating: ratings) {
            Integer ratingInt = FormUtils.ratingToInteger(rating.getText());
            if (ratingInt > highestRatingInt) {
                highestRatingInt = ratingInt;
                highestRating = ratingIntToText(highestRatingInt);
            }

        }

        return highestRating;

    }

}
