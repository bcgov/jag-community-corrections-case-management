package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface ClientMapper {

    @Mapping(target = "clientNum", source = "client.clientNo")
    @Mapping(target = "clientName", source = "client.clientName")
    @Mapping(target = "gender", source = "client.genderCode")
    @Mapping(target = "birthDate", source = "client.birthDate")
    @Mapping(target = "clientAge", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.calculateAge(client.getBirthDate()))")
    @Mapping(target = "address", source = "address")
    Client toApiClient(ca.bc.gov.open.jag.api.model.Client client, List<ca.bc.gov.open.jag.api.model.Address> address);


    @Mapping(target = "street", source = "addressLine1Txt")
    @Mapping(target = "city", source = "cityCd")
    @Mapping(target = "postalCode", source = "postalCodeTxt")
    @Mapping(target = "sealed", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.yesNoToBool(address.getSealedYn()))")
    @Mapping(target = "type", source = "ddtyCd")
    @Mapping(target = "expired", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.isExpired(address.getExpiryDt()))")
    Address toAddress(ca.bc.gov.open.jag.api.model.Address address);

}
