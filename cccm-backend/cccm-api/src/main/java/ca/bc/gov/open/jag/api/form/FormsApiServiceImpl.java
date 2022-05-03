package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormQuestions;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class FormsApiServiceImpl implements FormsApi {

    @Override
    @Transactional
    public FormDetails addForm(@Valid FormDetails formDetails) {
        return formDetails;
    }

    @Override
    @Transactional
    public void deleteForm(BigDecimal formId) {
        //TODO implement when ready
    }

    @Override
    @Transactional
    public FormDetails getForm(BigDecimal formId) {
        return createMockForm();
    }

    @Override
    @Transactional
    public FormList getForms() {

        FormList formList = new FormList();

        formList.setItems(Collections.singletonList(createMockForm()));

        return formList;

    }

    @Override
    @Transactional
    public FormDetails updateForm(@Valid FormDetails formDetails) {
        return formDetails;
    }

    /**
     * This for mocking data until we get the database
     * @return mock formdetails
     */
    private FormDetails createMockForm() {
        FormDetails formDetails = new FormDetails();

        formDetails.setCompletedDate(LocalDate.of(2021, 12, 14));
        formDetails.setCreatedBy("Doe, Jane");
        formDetails.setCreatedDate(LocalDate.of(2021, 12, 14));
        formDetails.setFormId(BigDecimal.ONE);
        formDetails.setFormType("Initial Assessment");
        formDetails.setUpdateDate(LocalDate.of(2021, 12, 14));

        List<FormQuestions> formQuestionsList = new ArrayList<>();

        formQuestionsList.add(createFormQuestion(BigDecimal.ONE, "Test Question 1"));
        formQuestionsList.add(createFormQuestion(BigDecimal.TEN, "Test Question 2"));

        formDetails.setFormQuestions(formQuestionsList);

        return formDetails;
    }

    /**
     * This is for mocking data until we get the database
     * @param id identifier
     * @param text question text
     * @return populated question
     */
    private FormQuestions createFormQuestion(BigDecimal id, String text) {

        FormQuestions formQuestion = new FormQuestions();
        formQuestion.setQuestionId(id);
        formQuestion.setLabel(text);

        return formQuestion;

    }

}
