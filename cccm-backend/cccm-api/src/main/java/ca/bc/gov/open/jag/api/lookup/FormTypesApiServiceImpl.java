package ca.bc.gov.open.jag.api.lookup;

import ca.bc.gov.open.jag.cccm.api.openapi.FormtypesApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormType;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormTypeList;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.logging.Logger;

@ApplicationScoped
public class FormTypesApiServiceImpl implements FormtypesApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(FormTypesApiServiceImpl.class));

    @Override
    @Transactional
    public FormTypeList getFormTypes() {

        logger.info("Get form types request received");

        FormTypeList formTypeList = new FormTypeList();

        formTypeList.getItems().add(createFormType(BigDecimal.ONE, "IA", "Initial Assessment"));
        formTypeList.getItems().add(createFormType(BigDecimal.TEN, "AA", "Another Assessment"));

        return formTypeList;

    }

    /**
     * Create mock form type
     * @param id mock id
     * @param cd mock code
     * @param desc mock description
     * @return populated object
     */
    private FormType createFormType(BigDecimal id, String  cd, String desc) {

        FormType formType = new FormType();
        formType.setTypeId(id);
        formType.setTypeCd(cd);
        formType.setTypeDescription(desc);
        return formType;

    }

}
