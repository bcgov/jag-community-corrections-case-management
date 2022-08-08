package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.cccm.api.openapi.model.Form;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSearchList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface FormMapper {

    @Mapping(target = "items", source="forms")
    //Mapstruct cannot map the array directly. Dummy value is required to bypass
    FormSearchList toFormSearchList(String dummyValue, List<ca.bc.gov.open.jag.api.model.data.Form> forms);

    @Mapping(target = "type", source="formType")
    Form toForm(ca.bc.gov.open.jag.api.model.data.Form form);

}
