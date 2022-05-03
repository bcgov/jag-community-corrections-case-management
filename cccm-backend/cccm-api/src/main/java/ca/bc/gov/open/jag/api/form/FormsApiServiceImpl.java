package ca.bc.gov.open.jag.api.form;

import ca.bc.gov.open.jag.cccm.api.openapi.FormsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormQuestions;
import ca.bc.gov.open.jag.cccm.api.openapi.model.QuestionBody;

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

        formQuestionsList.add(createFormQuestion(BigDecimal.valueOf(1), "Test Question 1", "radio", "radio1", "right",false, false, true, "testLabel1", null,true));
        formQuestionsList.add(createFormQuestion(BigDecimal.valueOf(2), "Test Question 2", "textarea", "comments", null, null,true, true, null, false, false));
        formQuestionsList.add(createFormQuestion(BigDecimal.valueOf(3), "Test Question 3", "checkbox", "intervention", null, null,false, true, "false", null, false));

        formDetails.setFormQuestions(formQuestionsList);

        return formDetails;
    }

    /**
     * This is for mocking data until we get the database
     * @param id identifier
     * @param text question text
     * @return populated question
     */
    private FormQuestions createFormQuestion(BigDecimal id, String text, String type, String key,
                                             String position, Boolean inline, Boolean tableView,
                                             Boolean input, String defaultValue, Boolean autoExpand, Boolean questions) {

        FormQuestions formQuestion = new FormQuestions();
        formQuestion.setQuestionId(id);
        formQuestion.setLabel(text);
        formQuestion.setType(type);
        formQuestion.setKey(key);
        formQuestion.setInput(true);
        formQuestion.setOptionsLabelPosition(position);
        formQuestion.setInline(inline);
        formQuestion.setTableView(tableView);
        formQuestion.setInput(input);
        formQuestion.setDefaultValue(defaultValue);

        if (Boolean.TRUE.equals(questions)) {

            List<QuestionBody> questionBodyList = new ArrayList<>();
            questionBodyList.add(createBody("Test Label 1", "testLabel1", "A"));
            questionBodyList.add(createBody("Test Label 2", "testLabel2", "B"));
            questionBodyList.add(createBody("Test Label 3", "testLabel3", "C"));
            questionBodyList.add(createBody("Test Label 4", "testLabel4", "D"));
            formQuestion.setValues(questionBodyList);

        }

        return formQuestion;

    }

    private QuestionBody createBody(String label, String value, String shortcut) {

        QuestionBody questionBody = new QuestionBody();
        questionBody.setLabel(label);
        questionBody.setShortcut(shortcut);
        questionBody.setValue(value);
        return questionBody;

    }

}
