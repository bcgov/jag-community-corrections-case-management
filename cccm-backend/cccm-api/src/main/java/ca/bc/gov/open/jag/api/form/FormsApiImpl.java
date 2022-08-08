package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.api.mapper.FormMapper;
import ca.bc.gov.open.jag.api.model.Form;
import ca.bc.gov.open.jag.api.model.FormRequest;
import ca.bc.gov.open.jag.api.service.DataService;
import ca.bc.gov.open.jag.api.service.SpeedmentClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSearchList;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class FormsApiImpl implements FormsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(FormsApiImpl.class));

    @Inject
    DataService dataService;

    @Inject
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Inject
    FormMapper formMapper;

    @Override
    @Transactional
    @RolesAllowed("form-add")
    public FormDetails addForm(String formDetails) {

        logger.warning("Form add not implemented");
        logger.log(Level.INFO, formDetails);
        return new FormDetails();

    }

    @Override
    @Transactional
    @RolesAllowed("form-delete")
    public void deleteForm(BigDecimal formId) {

        logger.warning("Form delete not implemented");

        //TODO implement when ready
    }

    @Override
    @RolesAllowed("form-view")
    public FormDetails getFormById(BigDecimal formId) {

        logger.info("Get form id request received");

        return dataService.getFormRequest(new FormRequest(formId, null));

    }

    @Override
    @RolesAllowed("form-view")
    public FormDetails getFormByType(String formType) {

        logger.info("Get form type request received");

        return dataService.getFormRequest(new FormRequest(null, formType));

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public FormList getForms() {

        logger.info("Get forms request received");

        FormList formList = new FormList();

        formList.setItems(Collections.singletonList(dataService.getFormRequest(new FormRequest(BigDecimal.ONE, null))));

        return formList;

    }

    @Override
    @Transactional
    @RolesAllowed("form-update")
    public FormDetails updateForm(@Valid FormDetails formDetails) {

        logger.warning("Form update not implemented");

        return formDetails;

    }

    @Override
    @Transactional
    @RolesAllowed("form-view")
    public FormSearchList getFormSearch(@NotNull String clientNum, @NotNull Boolean currentPeriod, String formTypeCd) {

        List<Form> forms;

        if (StringUtils.isBlank(formTypeCd)) {
            forms = speedmentClientService.getFormsByClient(clientNum);
        } else {
            forms = speedmentClientService.getFormsByClient(clientNum, formTypeCd);
        }

        return formMapper.toFormSearchList("", forms);

    }

}
