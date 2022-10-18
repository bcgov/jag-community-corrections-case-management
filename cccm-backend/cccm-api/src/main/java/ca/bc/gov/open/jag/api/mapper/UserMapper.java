package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.cccm.api.openapi.model.PODashboard;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SupervisorDashboard;
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
    @Mapping(target = "designations", source = "popDesignations")
    @Mapping(target = "inCustody", source = "clientIn")
    @Mapping(target = "orderExpiryDate", source = "FOED")
    @Mapping(target = "supervisionRating", source = "supervisionLevel")
    @Mapping(target = "rnaCompletedDate", source = "completionDate")
    @Mapping(target = "dueNext", source = "dueNext")
    @Mapping(target = "dueDate", source = "dueDate")
    PODashboard toPODashboard(ca.bc.gov.open.jag.api.model.data.PODashboard poDashboard);

    List<SupervisorDashboard> toSupervisorDashboardList(List<ca.bc.gov.open.jag.api.model.data.SupervisorDashboard> supervisorDashboardList);

    @Mapping(target = "officer", source = "poName")
    @Mapping(target = "activeAdmin", source = "activeAdmin")
    @Mapping(target = "bal", source = "bAL")
    @Mapping(target = "high", source = "high")
    @Mapping(target = "medium", source = "medium")
    @Mapping(target = "low", source = "low")
    @Mapping(target = "unknown", source = "unknown")
    @Mapping(target = "overdue", source = "overDueRNAs")
    @Mapping(target = "activeReports", source = "activeReports")
    SupervisorDashboard toSupervisorDashboard(ca.bc.gov.open.jag.api.model.data.SupervisorDashboard supervisorDashboard);

}
