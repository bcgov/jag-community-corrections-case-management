package ca.bc.gov.open.jag.api.service;


import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.model.FormRequest;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;

public interface FormDataService {

    FormDetails getFormRequest(FormRequest formRequest) throws CCCMException;

}
