package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.lookup.CodeTableType;
import ca.bc.gov.open.jag.api.mapper.CodeTableMapper;
import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequestScoped
public class CodeTableServiceImpl implements CodeTableService {

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    CodeTableMapper codeTableMapper;

    @Override
    public CodeList getCodes(CodeTableType type) {

        List<CodeTable> codes = new ArrayList<>();

        switch (type) {
            case FORM_TYPE:
                codes = Collections.emptyList();
                break;
            case LOCATION_TYPE:
                codes = Collections.emptyList();
                break;
            case GENDER_TYPE:
                codes = obridgeClientService.getGenderTypes();
                break;
            case ADDRESS_TYPE:
                codes = obridgeClientService.getAddressTypes();
                break;
            case IDENTIFIER_TYPE:
                codes = obridgeClientService.getIdentifierTypes();
                break;
        }

        return codeTableMapper.toCodeResult("dummyValue", codes);

    }


}
