package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.client.ClientsApiImpl;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.CloneFormRequest;
import ca.bc.gov.open.jag.api.model.service.UpdateForm;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static ca.bc.gov.open.jag.api.Keys.*;
import static ca.bc.gov.open.jag.api.error.CCCMErrorCode.VALIDATIONERROR;

@RequestScoped
public class FormsApiImpl implements FormsApi {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(FormsApiImpl.class));

    @Inject
    FormDataService formDataService;

    @Inject
    ClientDataService clientDataService;

    @Inject
    ClientFormSaveService clientFormSaveService;

    @Inject
    ValidationService validationService;

    @Inject
    JsonWebToken jwt;

    @Inject
    @Claim(standard = Claims.preferred_username)
    String username;

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getClientFormAnswersUsingGET(BigDecimal clientFormId, String clientNumber) {

        logger.info("Client Form Answers Request");

        return clientDataService.getClientFormAnswers(clientNumber, clientFormId);

    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public String saveClientFormAnswersUsingPUT(BigDecimal clientFormId, String clientNumber, Boolean loadLatestValues,
            String answerPayload) {

        logger.info("Save Client Form Answers Request");

        if (loadLatestValues == null) {
            loadLatestValues = Boolean.FALSE;
        }

        return clientDataService.saveClientFormAnswers(clientNumber, clientFormId, answerPayload, loadLatestValues);
    }

    @Override
    @Transactional
    @RolesAllowed("form-update")

    public void deleteInterventionsExceptUsingPUT(BigDecimal clientFormId, String clientNumber, String updatePayload) {

        logger.info("Delete Interventions Request");

        clientDataService.deleteInterventionsExcept(clientNumber, clientFormId, updatePayload);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getFormAsJSONUsingGET(BigDecimal clientFormId, String clientNumber, Boolean includeOptionValues) {

        logger.info("Form As Json Request");

        return clientDataService.getClientFormJSON(clientFormId, clientNumber, includeOptionValues);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getDecoratorContentUsingGET(String identifier) {

        logger.info("Decorator Content Request");

        return formDataService.getFormDecorator(identifier);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public List<ClientFormSummary> clientFormSearchUsingGET(String csNumber, Boolean currentPeriod, String formTypeCd) {

        logger.info("Form Search Request");

        return clientDataService.clientFormSearch(csNumber, currentPeriod, formTypeCd);

    }

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public BigDecimal cloneClientForm(String xLocationId, @Valid CloneForm cloneForm) {

        logger.info("Clone Client Form Request");

        return clientFormSaveService.cloneClientForm(new CloneFormRequest(cloneForm.getClientNumber(), cloneForm.getClientFormId(), new BigDecimal(xLocationId)), username);

    }

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public void completeForm(@Valid @NotNull UpdateFormInput createFormInput, String xLocationId) {

        logger.info("Complete Form Request");

        clientFormSaveService.editForm(new UpdateForm(createFormInput, new BigDecimal(xLocationId), hasOverride(), username, true));

    }

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public void editForm(@Valid @NotNull UpdateFormInput updateFormInput, String xLocationId) {

        logger.info("Edit Form Request");

        clientFormSaveService.editForm(new UpdateForm(updateFormInput, new BigDecimal(xLocationId), hasOverride(), username, false));

    }

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public void linkForm(@Valid @NotNull LinkFormInput updateFormInput, String xLocationId) {

        logger.info("Link Form Request");

        if (updateFormInput.getLinkedClientFormId() == null) {
            throw new CCCMException("Linked form id is required", VALIDATIONERROR);
        }
        clientFormSaveService.linkForm(updateFormInput, new BigDecimal(xLocationId));

    }

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public BigDecimal createCrnaForm(@Valid @NotNull CreateFormInput createFormInput, String xLocationId) {

        logger.info("Create CRNA Form Request");

        return clientFormSaveService.createCRNA(createFormInput, new BigDecimal(xLocationId));

    }

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public BigDecimal createSaraForm(@Valid @NotNull CreateFormInput createFormInput, String xLocationId) {

        logger.info("Create SARA Form Request");

        return clientFormSaveService.createSARA(createFormInput, new BigDecimal(xLocationId));

    }

    @Override
    @Transactional
    @RolesAllowed("form-delete")
    public void deleteClientForm(BigDecimal clientFormId, String clientNum, String xLocationId) {

        logger.info("Delete Form Request");

        clientFormSaveService.deleteForm(clientFormId, clientNum, new BigDecimal(xLocationId), username, hasOverride());

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public ClientFormSummary getClientFormSummaryUsingGET(BigDecimal clientFormId, String clientNumber) {

        logger.info("Client Form Summary Request");

        return clientDataService.getClientFormSummary(clientFormId, clientNumber);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getClientFormAnswersSummaryJSONUsingGET(BigDecimal clientFormId, String clientNumber, Boolean includeLinkedForm) {

        logger.info("Client Form Answer Summary Request");

        return clientDataService.getClientFormAnswersSummary(clientNumber, clientFormId, includeLinkedForm);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public List<FormSummary> getFormSummaries(String module, Boolean latestOnly) {

        logger.info("Form Summaries Request");

        return formDataService.getFormSummaries(module, latestOnly);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getClientFormAnswersForSectionAndQuestionUsingGET(BigDecimal clientFormId, String clientNumber,
            Integer questionSequence, Integer sectionSequence) {

        logger.info("Client Form Answer For Section and Question Request");

        return clientDataService.getClientFormAnswersForSectionAndQuestion(clientNumber, clientFormId, sectionSequence,
                questionSequence);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getClientFormAnswersForSectionUsingGET(BigDecimal clientFormId, String clientNumber,
            Integer sectionSequence) {

        logger.info("Client Form Answer For Section Request");

        return clientDataService.getClientFormAnswersForSection(clientNumber, clientFormId, sectionSequence);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public List<Intervention> searchClientInterventionsUsingPOST(ClientSearchInput searchInput) {

        logger.info("Client Interventions Section Request");

        return clientDataService.searchClientFormInterventions(searchInput);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public List<Comment> searchClientCommentsUsingPOST( ClientSearchInput searchInput) {

        logger.info("Search Client Comments Request");

        return clientDataService.searchClientFormComments( searchInput);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public List<Responsivity> searchClientResponsivitiesUsingPOST(ClientSearchInput searchInput) {

        logger.info("Search Responsivities Request");

        return clientDataService.searchClientFormResponsivities(searchInput);

    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public void upcertResponsivities(BigDecimal clientFormId, String payload) {

        logger.info("Upcert Responsivities Request");

        clientDataService.upcertResponsivities(clientFormId, payload);

    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public void updateForm(BigDecimal clientFormId, String createFormInput, String xLocationId) {

        logger.info("Update Form Request");

        clientFormSaveService.updateForm(clientFormId, createFormInput);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getClientFormIntervention(String csNumber, BigDecimal clientFormId, Boolean includeLinkedForm, String xLocationId) {

        logger.info("Client Form Interventions Request");

        return clientDataService.getClientFormIntervetionForCasePlan(csNumber, clientFormId, includeLinkedForm);

    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public void updateSourcesContacted(BigDecimal clientFormId, String sourcesContacted, String xLocationId) {

        logger.info("Sources Contacted Request");

        clientDataService.updateSourcesContacted(clientFormId, sourcesContacted);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public ValidationResult validateCRNAForm(@Valid @NotNull String body) {

        logger.info("Validate CRNA Request");

        return validationService.validateCRNA(body);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public ValidationResult validateSARAForm(@Valid @NotNull String body) {

        logger.info("Validate SARA Request");

        return validationService.validateSARA(body);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getClientFormMetaJson(String csNumber, BigDecimal clientFormId, String xLocationId) {

        logger.info("Client Form Meta Request");

        return clientDataService.getClientFormMetaJson(csNumber, clientFormId);

    }

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public BigDecimal createACUTEForm(@Valid @NotNull CreateFormInput createFormInput, String xLocationId) {

        logger.info("Create ACUTE Request");

        return clientFormSaveService.createACUTE(createFormInput, new BigDecimal(xLocationId));

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public ValidationResult validateACUTEForm(@Valid @NotNull String body) {

        logger.info("Validate ACUTE Request");

        return validationService.validateACUTE(body);

    }

    private Boolean hasOverride() {

        JsonObject realmAccess = (JsonObject)jwt.claim(JWT_REALM_ACCESS).get();

        List<String> roles = realmAccess.getJsonArray(JWT_REALM_ROLES)
                .stream()
                .map(value -> ((JsonString)value).getString())
                .collect(Collectors.toList());

        return (roles.contains(JWT_ROLE));

    }

}
