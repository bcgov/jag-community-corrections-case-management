package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormType;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormTypeList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Location;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface CodeTableMapper {

    @Mapping(target = "items", source = "codes")
    FormTypeList toFormTypes(String dummyValue, List<CodeTable> codes);

    @Mapping(target = "typeCd", source = "code")
    @Mapping(target = "typeDescription", source = "value")
    FormType toFormType(CodeTable codeTable);

    @Mapping(target = "items", source = "codes")
    LocationList toLocations(String dummyValue, List<CodeTable> codes);

    @Mapping(target = "locationCd", source = "code")
    @Mapping(target = "locationDescription", source = "value")
    Location toLocation(CodeTable codeTable);

}
