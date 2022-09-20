package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.api.model.data.Location;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
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

}
