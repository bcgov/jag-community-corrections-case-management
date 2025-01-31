package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.data.CloneFormRequest;
import ca.bc.gov.open.jag.api.model.service.UpdateForm;
import ca.bc.gov.open.jag.api.service.ClientDataService;
import ca.bc.gov.open.jag.api.service.ClientFormSaveService;
import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.ValidationService;
import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collections;
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
    public String getClientFormAnswersUsingGET(BigDecimal clientFormId, String clientNumber, String xLocationId) {

        logger.info("Client Form Answers Request");

        return clientDataService.getClientFormAnswers(clientNumber, clientFormId, xLocationId);

    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public String saveClientFormAnswersUsingPUT(BigDecimal clientFormId, String clientNumber, Boolean loadLatestValues,
            String answerPayload, String xLocationId) {

        logger.info("Save Client Form Answers Request");

        if (loadLatestValues == null) {
            loadLatestValues = Boolean.FALSE;
        }

        return clientDataService.saveClientFormAnswers(clientNumber, clientFormId, answerPayload, false, loadLatestValues, xLocationId);
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
    public String getFormAsJSONUsingGET(BigDecimal clientFormId, String clientNumber, String xLocationId, Boolean includeOptionValues) {

        logger.info("Form As Json Request");

        return clientDataService.getClientFormJSON(clientFormId, clientNumber, includeOptionValues, xLocationId);

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
    public List<ClientFormSummary> clientFormSearchUsingGET(String csNumber, Boolean currentPeriod, String formTypeCd, String xLocationId, Boolean mostRecent) {

        logger.info("Form Search Request");

        return clientDataService.clientFormSearch(csNumber, currentPeriod,mostRecent, formTypeCd, xLocationId);

    }

    @Override
    @Transactional
    @RolesAllowed("form-clone")
    public BigDecimal cloneClientForm(String xLocationId, @Valid CloneForm cloneForm) {

        logger.info("Clone Client Form Request");

        return clientFormSaveService.cloneClientForm(new CloneFormRequest(cloneForm.getClientNumber(), cloneForm.getClientFormId(), new BigDecimal(xLocationId), checkOverride(cloneForm.getModule())), username, xLocationId);

    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public void completeForm(@Valid @NotNull UpdateFormInput completeFormInput, String xLocationId) {

        logger.info("Complete Form Request");

        clientFormSaveService.editForm(new UpdateForm(completeFormInput, new BigDecimal(xLocationId), checkOverride(completeFormInput.getModule()), username, true, completeFormInput.getInterventionCheckboxChecked()), xLocationId);

    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public void editForm(@Valid @NotNull UpdateFormInput updateFormInput, String xLocationId) {

        logger.info("Edit Form Request");

        clientFormSaveService.editForm(new UpdateForm(updateFormInput, new BigDecimal(xLocationId),  checkOverride(updateFormInput.getModule()), username, false, Collections.EMPTY_LIST), xLocationId);

    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
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

        clientFormSaveService.deleteForm(clientFormId, clientNum, new BigDecimal(xLocationId), username, hasOverride(JWT_ROLE));

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public ClientFormSummary getClientFormSummaryUsingGET(BigDecimal clientFormId, String clientNumber, String xLocationId) {

        logger.info("Client Form Summary Request");

        return clientDataService.getClientFormSummary(clientFormId, clientNumber, xLocationId);

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public String getClientFormAnswersSummaryJSONUsingGET(BigDecimal clientFormId, String clientNumber, Boolean includeLinkedForm, String xLocationId) {

        logger.info("Client Form Answer Summary Request");

        return clientDataService.getClientFormAnswersSummary(clientNumber, clientFormId, includeLinkedForm, xLocationId);

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

        return clientDataService.getClientFormIntervetionForCasePlan(csNumber, clientFormId, includeLinkedForm, xLocationId);

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

        return validationService.validateCRNA(body, Collections.EMPTY_LIST, true);

    }

    @Override
    @RolesAllowed("form-view")
    public ValidationResult validateCmrpForm(String body) {

        logger.info("Validate CMRP Request");

        return validationService.validateCMRP(body, null, null);

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

        return clientDataService.getClientFormMetaJson(csNumber, clientFormId, xLocationId);

    }

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public BigDecimal createACUTEForm(@Valid @NotNull CreateFormInput createFormInput, String xLocationId) {

        logger.info("Create ACUTE Request");

        return clientFormSaveService.createACUTE(createFormInput, new BigDecimal(xLocationId));

    }

    @Override
    @RolesAllowed("form-add")
    public BigDecimal createCmrpForm(CreateFormInput createFormInput, String xLocationId) {

        logger.info("Create CMRP Request");

        return clientFormSaveService.createCMRP(createFormInput, new BigDecimal(xLocationId));

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public ValidationResult validateACUTEForm(@Valid @NotNull String body) {

        logger.info("Validate ACUTE Request");

        return validationService.validateACUTE(body);

    }

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public BigDecimal createStatic99rForm(@Valid @NotNull CreateFormInput createFormInput, String xLocationId) {

        logger.info("Create Static99r Request");

        return clientFormSaveService.createStatic99r(createFormInput, new BigDecimal(xLocationId));

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public ValidationResult validateStatic99rForm(@Valid @NotNull String body) {

        logger.info("Validate Static99r Request");

        return validationService.validateStatic99r(body);

    }

    @Override
    @RolesAllowed("form-add")
    public BigDecimal createOverallForm(@Valid @NotNull CreateFormInput createFormInput, String xLocationId) {

        logger.info("Create Overall Request");

        return clientFormSaveService.createSOOverall(createFormInput, new BigDecimal(xLocationId));

    }


    @Override
    @RolesAllowed("form-add")
    public BigDecimal createStableForm(@Valid @NotNull CreateFormInput createFormInput, String xLocationId) {

        logger.info("Create Stable Request");

        return clientFormSaveService.createStable(createFormInput, new BigDecimal(xLocationId));

    }

    @Override
    @RolesAllowed("form-view")
    public ValidationResult validateOverallForm(@Valid @NotNull String body) {

        logger.info("Validate Overall Request");

        return validationService.validateSOOverall(body, "",true);

    }

    @Override
    @RolesAllowed("form-view")
    public ValidationResult validateStableForm(@Valid @NotNull String body) {

        logger.info("Validate Stable Request");

        return validationService.validateStable(body);

    }

    private Boolean hasOverride(String role) {

        JsonObject realmAccess = (JsonObject)jwt.claim(JWT_REALM_ACCESS).get();

        List<String> roles = realmAccess.getJsonArray(JWT_REALM_ROLES)
                .stream()
                .map(value -> ((JsonString)value).getString())
                .collect(Collectors.toList());

        return (roles.contains(role));

    }

    private Boolean checkOverride(String module) {

        if (hasOverride(JWT_ROLE)) {
            return true;
        } else if (hasOverride(CMRP_OVERRIDE_ROLE) && StringUtils.equals(module, CUSTODY_CMRP_FORM_TYPE)) {
            return true;
        }

        return false;

    }

}
