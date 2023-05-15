package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface UserMapper {

    List<PODashboard> toPODashboardList(List<ca.bc.gov.open.jag.api.model.data.PODashboard> poDashboardList);

    @Mapping(target = "clientNum", source = "clientNo")
    @Mapping(target = "clientName", source = "clientName")
    @Mapping(target = "alerts", source = "alerts")
    @Mapping(target = "warrants", source = "activeDocuments")
    @Mapping(target = "designations", source = "ucmpDesignations")
    @Mapping(target = "inCustody", source = "clientIn")
    @Mapping(target = "orderExpiryDate", source = "FOED")
    @Mapping(target = "supervisionRating", source = "supervisionLevel")
    @Mapping(target = "rnaCompletedDate", source = "RNACompleted")
    @Mapping(target = "dueNext", source = "dueNext")
    @Mapping(target = "dueDate", source = "dueDate")
    PODashboard toPODashboard(ca.bc.gov.open.jag.api.model.data.PODashboard poDashboard);

    List<SupervisorDashboard> toSupervisorDashboardList(List<ca.bc.gov.open.jag.api.model.data.SupervisorDashboard> supervisorDashboardList);

    @Mapping(target = "officer", source = "poName")
    @Mapping(target = "idirId", source = "idirId")
    @Mapping(target = "activeAdmin", source = "activeAdmin")
    @Mapping(target = "bal", source = "bAL")
    @Mapping(target = "sen",  source = "sent")
    @Mapping(target = "high", source = "high")
    @Mapping(target = "medium", source = "medium")
    @Mapping(target = "low", source = "low")
    @Mapping(target = "unknown", source = "unknown")
    @Mapping(target = "overdue", source = "overDueRNAs")
    @Mapping(target = "activeReports", source = "activeReports")
    @Mapping(target = "notRequired", source = "notRequired")
    @Mapping(target = "adminClosed", source = "adminClosed")
    SupervisorDashboard toSupervisorDashboard(ca.bc.gov.open.jag.api.model.data.SupervisorDashboard supervisorDashboard);

    @Mapping(target = "pcm", source = "PCM")
    @Mapping(target = "scm", source = "SCM")
    @Mapping(target = "smo", source = "SMO")
    @Mapping(target = "closedIncomplete", source = "closedIncomplete")
    @Mapping(target = "expiringThirty", source = "expiringThirty")
    @Mapping(target = "dueSeven", source = "notRequired")
    SupervisorDashboardDetails toSupervisorDashboardDetails(ca.bc.gov.open.jag.api.model.data.SupervisorDashboardDetails supervisorDashboardDetails);

    User toUser(ca.bc.gov.open.jag.api.model.data.User user);

    List<User> toUserList(List<ca.bc.gov.open.jag.api.model.data.User> users);

    List<PO> toPOList(List<ca.bc.gov.open.jag.api.model.data.User> users);

    @Mapping(target = "poName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "idirId", source = "idirId")
    PO toPO(ca.bc.gov.open.jag.api.model.data.User user);


}
