package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.CloneFormRequest;
import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;

import java.math.BigDecimal;
import java.util.List;

public interface ClientDataService {

    List<Client> clientSearch(ClientSearch clientSearch);

    List<Client> clientAddressSearch(ClientAddressSearch clientAddressSearch);

    Client clientProfile(String clientNum, String user, String location);

    Photo clientPhoto(String clientNum);

    List<Address> clientAddress(String clientNum, String user, String location);

    Client clientDetails(String clientNum, String user, String location);

    /**
     * Save client form answers
     * @param clientNumber
     * @param clientFormId the form Id
     * @param payload the JSON representation of the form data
     * @param loadLatestAnswers set true to return latest answers - has performance overhead so only use if saving the whole form, not a single question
     *
     * @return {@link String} JSON value of answers if loadLatest is requested
     */
    String saveClientFormAnswers(String clientNumber, BigDecimal clientFormId, String payload, boolean loadLatestAnswers);


    /**
     * Perform an update on interventions - basically deletions in a round-about sort of way
     * @param clientNumber
     * @param clientFormId
     * @param payload
     */
    void deleteInterventionsExcept(String clientNumber, BigDecimal clientFormId, String payload);

    /**
     * Get client form answers
     * @param clientNumber
     * @param clientFormId
     * @return {@link String} JSON answer object
     */
    String getClientFormAnswers(String clientNumber, BigDecimal clientFormId);

    List<ClientFormSummary> clientFormSearch(String clientNum, boolean currentPeriod, String formTypeCd);

    BigDecimal addClientForm(CreateFormInput createFormInput);

    BigDecimal cloneClientForm(CloneFormRequest cloneFormRequest);

    String getClientFormJSON(BigDecimal clientFormId,String clientNumber,  boolean includeValues);

    ClientFormSummary getClientFormSummary(BigDecimal clientFormId, String clientNumber);

    /**
     * Get all client form answers for a client form and section sequence (.e. S03 )
     * @param clientNumber client csNumber
     * @param clientFormId unique client form id
     * @param sectionSequence 1-based
     * @return JSON object for answers
     */
    String getClientFormAnswersForSection(String clientNumber, BigDecimal clientFormId, int sectionSequence);

    /**
     * Get single client form answer for form/section/sequence
     * @param clientNumber client csNumber
     * @param clientFormId unique client form id
     * @param sectionSequence 1-based
     * @param questionSequence 1-based
     * @return JSON object for answers
     */
    String getClientFormAnswersForSectionAndQuestion(String clientNumber, BigDecimal clientFormId, int sectionSequence, int questionSequence);

    /**
     * Get possible form factors (sections) for a given report type and client
     * @param reportType
     * @param csNumber
     * @return {@link <List<LabelValuePair>>}
     */
    List<LabelValuePair> getClientFormFactors( String reportType,  String csNumber);



    /**
     * As is says on the can - get responsivities (emotional/social response to some input - e.g an intervention plan)
     * @param searchInput {@link ClientSearchInput}
     * @return {@link List<Responsivity>}
     */
    List<Responsivity> searchClientFormResponsivities(ClientSearchInput searchInput);

    /**
     * Get interventions
     * @param searchInput {@link ClientSearchInput}
     * @return {@link List<Intervention>}
     */
    List<Intervention> searchClientFormInterventions(ClientSearchInput searchInput);

    /**
     * Get comments related to form questions
     * @param searchInput {@link ClientSearchInput}
     * @return {@link List<Comment>}
     */
    List<Comment> searchClientFormComments(ClientSearchInput searchInput);

    /**
     * Get summary information for a form
     * @param clientNumber
     * @param clientFormId
     * @return Submitted answers for a form - server call to ensure latest data always displayed
     * as is saved in the database
     */
    String getClientFormAnswersSummary(String clientNumber, BigDecimal clientFormId, Boolean includeLinkedForm);
    
    
    /**
     * Gets the client form intervetion for case plan.
     *
     * @param csNumber the cs number
     * @param clientFormId the client form id
     * @param includeLinkedForm the include linked form
     * @return the client form intervetion for case plan
     */
    String getClientFormIntervetionForCasePlan(String csNumber, BigDecimal clientFormId, boolean includeLinkedForm);
    
    /**
     * Gets the client form meta json.
     *
     * @param csNumber the cs number
     * @param clientFormId the client form id
     * @return the client form meta json
     */
    String getClientFormMetaJson(String csNumber, BigDecimal clientFormId);
    
    /**
     * Update sources contacted.
     *
     * @param clientFormId the client form id
     * @param sourcesContacted the sources contacted
     */
    void updateSourcesContacted(BigDecimal clientFormId, String sourcesContacted);
}
