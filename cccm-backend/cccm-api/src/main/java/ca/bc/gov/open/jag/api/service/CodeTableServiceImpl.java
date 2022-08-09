package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.mapper.CodeTableMapper;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormTypeList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationList;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

@ApplicationScoped
public class CodeTableServiceImpl implements CodeTableService {

    @Inject
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Inject
    CodeTableMapper codeTableMapper;

    @Override
    public FormTypeList formTypeCodes() {
        return codeTableMapper.toFormTypes("dummyValue", speedmentClientService.getFormTypes());
    }

    @Override
    public LocationList locationCodes() {
        return codeTableMapper.toLocations("dummyValue", speedmentClientService.getLocation());
    }

}
