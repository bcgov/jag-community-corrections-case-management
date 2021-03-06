package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.api.model.ClientProfile;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Address;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Alert;
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
    @Mapping(target = "custodyLocation", source = "clientProfile.custodyLocation")
    @Mapping(target = "communityLocation", source = "clientProfile.communityLocation")
    @Mapping(target = "status", source = "clientProfile.status")
    @Mapping(target = "caseManager", source = "clientProfile.caseManager")
    @Mapping(target = "secondaryManager", source = "clientProfile.secondaryManager")
    @Mapping(target = "supervisionLevel", source = "clientProfile.supervisionLevel")
    @Mapping(target = "orderInformation.orders", source = "clientProfile.orders")
    @Mapping(target = "orderInformation.expiryDate", source = "clientProfile.finalOrderExpiryDt")
    //TODO orderInformation requires efeective and due dates
    //TODO courtInformation is required
    @Mapping(target = "generalInformation.institution", source = "clientProfile.custodyLocation")
    @Mapping(target = "generalInformation.status", source = "clientProfile.institutionstatus")
    @Mapping(target = "generalInformation.type", source = "clientProfile.clientType")
    //TODO generalInformation requires discharge and parole dates and custody
    @Mapping(target = "locationInformation.internalLocation", source = "clientProfile.internalLocation")
    @Mapping(target = "locationInformation.federalParole", source = "clientProfile.federalParolOffice")
    @Mapping(target = "locationInformation.outLocation", source = "clientProfile.outLocation")
    @Mapping(target = "locationInformation.outReason", source = "clientProfile.outReason")
    //TODO locationInformation requires date
    @Mapping(target = "outstandingWarrants", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.stringToWarrantList(clientProfile.getWarrants()))")
    @Mapping(target = "designations", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.createDesignations(clientProfile.getIaStatus(),clientProfile.getPopDesignation(),clientProfile.getIcayraSecurity(),clientProfile.getIcayraSecurityStatus()))")
    @Mapping(target = "biometric.type", source = "clientProfile.isBiometricEnrolled")
    @Mapping(target = "biometric.status", source = "clientProfile.biometricStatus")
    @Mapping(target = "biometric.eServices", source = "clientProfile.eServicesStatus")
    @Mapping(target = "biometric.eReporting", source = "clientProfile.eReporting")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "communityAlerts", source="alerts")
    Client toApiClient(ca.bc.gov.open.jag.api.model.Client client, ClientProfile clientProfile, List<ca.bc.gov.open.jag.api.model.Address> address, List<ca.bc.gov.open.jag.api.model.Alert> alerts);


    @Mapping(target = "street", source = "addressLine1Txt")
    @Mapping(target = "city", source = "cityCd")
    @Mapping(target = "postalCode", source = "postalCodeTxt")
    @Mapping(target = "sealed", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.yesNoToBool(address.getSealedYn()))")
    @Mapping(target = "type", source = "ddtyCd")
    @Mapping(target = "expired", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.isExpired(address.getExpiryDt()))")
    Address toAddress(ca.bc.gov.open.jag.api.model.Address address);

    @Mapping(target = "comment", source = "commentTxt")
    @Mapping(target = "date", source = "effectiveDt")
    Alert toAlert(ca.bc.gov.open.jag.api.model.Alert alert);

}
