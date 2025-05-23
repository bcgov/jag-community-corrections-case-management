package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface ClientMapper {

    @Mapping(target = "clientNum", source = "client.clientNo")
    @Mapping(target = "clientName", source = "client.clientName")
    @Mapping(target = "currentNameYn", source = "client.currentNameYn")
    @Mapping(target = "gender", source = "client.genderCode")
    @Mapping(target = "birthDate", source = "client.birthDate")
    @Mapping(target = "sealed", source = "client.sealed")
    @Mapping(target = "clientAge", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.calculateAge(client.getBirthDate()))")
    @Mapping(target = "custodyLocation", source = "client.custodyLocation")
    @Mapping(target = "supervisionLevel", source = "clientProfile.supervisionLevel")
    @Mapping(target = "iaClassifications", source = "clientProfile.iaClassification")
    @Mapping(target = "outstandingWarrants", source = "clientProfile.warrants")
    @Mapping(target = "programs", source = "clientProfile.programs")
    @Mapping(target = "address", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.stringToAddressList(client.getAddress(), client.getAddressType(), client.getAddressExpiry()))")
    @Mapping(target = "communityAlerts", source="clientProfile.alerts")
    @Mapping(target = "rnaStatus", source="clientProfile.rnaStatus")
    @Mapping(target = "photo", source="photo")
    //@Mapping(target = "designations", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.createDesignations(clientProfile.getIaStatus(),clientProfile.getPopDesignation(),clientProfile.getIcayraSecurity(),clientProfile.getIcayraSecurityStatus()))")
    @Mapping(target = "designations", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.createUcmpDesignations(clientProfile.getUcmpDesignation()))")
    @Mapping(target = "populationDesignations", source = "clientProfile.popDesignation")
    //Community Information
    @Mapping(target = "communityInformation.communityLocation", source = "client.communityLocation")
    @Mapping(target = "communityInformation.status", source = "clientProfile.status")
    @Mapping(target = "communityInformation.caseManager", source = "client.caseManager")
    @Mapping(target = "communityInformation.secondaryManager", source = "clientProfile.secondaryManager")
    //Order Information
    @Mapping(target = "orderInformation.orders", source = "clientProfile.orders")
    @Mapping(target = "orderInformation.expiryDate", source = "clientProfile.finalOrderExpiryDt")
    @Mapping(target = "orderInformation.effectiveDate", source = "clientProfile.orderEffectiveDt")
    @Mapping(target = "orderInformation.dueDate", source = "clientProfile.nextDueDt")
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
    @Mapping(target = "locationInformation.nextCourtDate", source = "clientProfile.nextCourtDate")
    @Mapping(target = "locationInformation.warrantTypes", source = "clientProfile.warrantTypes")
    //Biometric
    @Mapping(target = "biometric.type", source = "clientProfile.isBiometricEnrolled")
    @Mapping(target = "biometric.status", source = "clientProfile.biometricStatus")
    @Mapping(target = "biometric.eServices", source = "clientProfile.eServicesStatus")
    @Mapping(target = "biometric.eReporting", source = "clientProfile.eReporting")
    Client toApiClient(ca.bc.gov.open.jag.api.model.data.Client client, ClientProfile clientProfile, ca.bc.gov.open.jag.api.model.data.Photo photo);

    @Mapping(target = "clientName", source = "client.clientName")
    @Mapping(target = "clientNum", source = "client.clientNo")
    @Mapping(target = "gender", source = "client.genderCode")
    @Mapping(target = "alias", source = "client.alias")
    @Mapping(target = "address", source="address")
    @Mapping(target = "photo", source="photo")
    @Mapping(target = "communityInformation.communityLocation", source = "client.communityLocation")
    @Mapping(target = "communityInformation.caseManager", source = "client.caseManager")
    Client toClientDetails(ca.bc.gov.open.jag.api.model.data.Client client, List<ca.bc.gov.open.jag.api.model.data.Address> address, ca.bc.gov.open.jag.api.model.data.Photo photo);

    @Mapping(target = "comment", source = "description")
    @Mapping(target = "date", source = "effectiveDate")
    Alert toAlert(ca.bc.gov.open.jag.api.model.data.Alert alert);

    @Mapping(target = "image", source = "image")
    @Mapping(target = "photoTakenDate", source = "photoTakenDate")
    Photo toPhoto(ca.bc.gov.open.jag.api.model.data.Photo photo);

    @Mapping(target = "courtFile", source = "courtFileNumber")
    @Mapping(target = "date", source = "issuedDate")
    @Mapping(target = "type", source = "charge")
    Warrant toWarrant(ca.bc.gov.open.jag.api.model.data.Warrant warrant);

    @Mapping(target = "name", source = "programName")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "referredDate", source = "referralDate")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "outcome", source = "outcome")
    Program toProgram(ca.bc.gov.open.jag.api.model.data.Program program);

    List<Address> toAddressList(List<ca.bc.gov.open.jag.api.model.data.Address> address);

    @Mapping(target = "fullAddress", source = "fullAddress")
    @Mapping(target = "expired", expression = "java(ca.bc.gov.open.jag.api.util.MappingUtils.isExpired(address.getExpiryDate()))")
    @Mapping(target = "primary", defaultValue = "false")
    Address toAddress(ca.bc.gov.open.jag.api.model.data.Address address);

    @Mapping(target = "key", source = "key")
    InterventionsChecked toInterventionChecked(ca.bc.gov.open.jag.api.model.data.Intervention intervention);

    List<InterventionsChecked> toInterventionsChecked(List<ca.bc.gov.open.jag.api.model.data.Intervention> interventions);

}
