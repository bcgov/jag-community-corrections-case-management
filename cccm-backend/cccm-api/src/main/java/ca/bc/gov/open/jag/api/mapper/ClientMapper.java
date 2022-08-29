package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Alert;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Photo;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Warrant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "cdi")
public interface ClientMapper {

    @Mapping(target = "clientNum", source = "client.clientNo")
    @Mapping(target = "clientName", source = "client.clientName")
    @Mapping(target = "gender", source = "client.genderCode")
    @Mapping(target = "birthDate", source = "client.birthDate")
    @Mapping(target = "clientAge", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.calculateAge(client.getBirthDate()))")
    @Mapping(target = "custodyLocation", source = "client.custodyLocation")
    @Mapping(target = "supervisionLevel", source = "clientProfile.supervisionLevel")
    @Mapping(target = "outstandingWarrants", source = "clientProfile.warrants")
    @Mapping(target = "address", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.stringToAddressList(client.getAddress()))")
    @Mapping(target = "communityAlerts", source="alerts")
    @Mapping(target = "designations", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.createDesignations(clientProfile.getIaStatus(),clientProfile.getPopDesignation(),clientProfile.getIcayraSecurity(),clientProfile.getIcayraSecurityStatus()))")
    //Community Information
    @Mapping(target = "communityInformation.communityLocation", source = "client.communityLocation")
    @Mapping(target = "communityInformation.status", source = "clientProfile.status")
    @Mapping(target = "communityInformation.caseManager", source = "client.caseManager")
    @Mapping(target = "communityInformation.secondaryManager", source = "clientProfile.secondaryManager")
    //Order Information
    @Mapping(target = "orderInformation.orders", source = "clientProfile.orders")
    @Mapping(target = "orderInformation.expiryDate", source = "clientProfile.finalOrderExpiryDt")
    //TODO orderInformation requires effective and due dates
    //TODO courtInformation is required
    //General Information
    @Mapping(target = "generalInformation.institution", source = "client.custodyLocation")
    @Mapping(target = "generalInformation.status", source = "clientProfile.institutionstatus")
    @Mapping(target = "generalInformation.type", source = "clientProfile.clientType")
    @Mapping(target = "generalInformation.custody", source = "clientProfile.custody")
    @Mapping(target = "generalInformation.paroleDate", source = "clientProfile.possibleParoleDt")
    @Mapping(target = "generalInformation.dischargeDate", source = "clientProfile.probableDischargeDt")
    //Location Information
    @Mapping(target = "locationInformation.internalLocation", source = "clientProfile.internalLocation")
    @Mapping(target = "locationInformation.federalParole", source = "clientProfile.federalParolOffice")
    @Mapping(target = "locationInformation.outLocation", source = "clientProfile.outLocation")
    @Mapping(target = "locationInformation.outReason", source = "clientProfile.outReason")
    @Mapping(target = "locationInformation.warrantExpiryDate", source = "clientProfile.finalWarrantExpiryDt")
    //Biometric
    @Mapping(target = "biometric.type", source = "clientProfile.isBiometricEnrolled")
    @Mapping(target = "biometric.status", source = "clientProfile.biometricStatus")
    @Mapping(target = "biometric.eServices", source = "clientProfile.eServicesStatus")
    @Mapping(target = "biometric.eReporting", source = "clientProfile.eReporting")
    Client toApiClient(ca.bc.gov.open.jag.api.model.data.Client client, ClientProfile clientProfile, List<ca.bc.gov.open.jag.api.model.data.Alert> alerts, BigDecimal clientId);

    @Mapping(target = "comment", source = "commentTxt")
    @Mapping(target = "date", source = "effectiveDt")
    Alert toAlert(ca.bc.gov.open.jag.api.model.data.Alert alert);

    @Mapping(target = "image", source = "photo")
    Photo toPhoto(String dummy, byte[] photo);

    @Mapping(target = "courtFile", source = "courtFileNumber")
    @Mapping(target = "date", source = "issuedDate")
    @Mapping(target = "type", source = "charge")
    Warrant toWarrant(ca.bc.gov.open.jag.api.model.data.Warrant warrant);

}
