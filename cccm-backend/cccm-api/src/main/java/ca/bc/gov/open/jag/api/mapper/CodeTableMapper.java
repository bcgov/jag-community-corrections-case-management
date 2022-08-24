package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface CodeTableMapper {

    @Mapping(target = "items", source = "codes")
    CodeList toCodeResult(String dummyValue, List<CodeTable> codes);

    @Mapping(target = "key", source = "code")
    @Mapping(target = "value", source = "value")
    Code toCode(CodeTable codeTable);


}
