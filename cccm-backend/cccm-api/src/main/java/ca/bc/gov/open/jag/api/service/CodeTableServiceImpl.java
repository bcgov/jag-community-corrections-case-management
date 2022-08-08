package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.mapper.CodeTableMapper;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormTypeList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationList;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;

public class CodeTableServiceImpl implements CodeTableService {

    @Inject
    @RestClient
    SpeedmentClientService speedmentClientService;

    @Inject
    CodeTableMapper codeTableMapper;

    @Override
    public FormTypeList getFormTypeCodeTable() {
        return codeTableMapper.toFormTypes("dummyValue", speedmentClientService.getFormTypes());
    }

    @Override
    public LocationList getLocationCodeTable() {
        return codeTableMapper.toLocations("dummyValue", speedmentClientService.getLocation());
    }

}
