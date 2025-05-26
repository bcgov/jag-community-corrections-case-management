package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.api.model.data.Location;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationCode;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LocationCodeList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface LocationMapper {

    @Mapping(target = "items", source = "locations")
    CodeList toCodeResult(String dummyValue, List<Location> locations);

    @Mapping(target = "key", source = "id")
    @Mapping(target = "value", source = "dsc")
    Code toCode(Location location);

    @Mapping(target = "items", source = "locations")
    LocationCodeList toLocationCodeResult(String dummyValue, List<Location> locations);

    @Mapping(target = "key", source = "id")
    @Mapping(target = "value", source = "dsc")
    @Mapping(target = "locationTypeCode", source = "lotyCd")
    LocationCode toLocationCode(Location location);

}
