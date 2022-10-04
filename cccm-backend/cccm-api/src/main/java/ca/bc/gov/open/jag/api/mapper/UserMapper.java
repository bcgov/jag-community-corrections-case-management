package ca.bc.gov.open.jag.api.mapper;

import ca.bc.gov.open.jag.cccm.api.openapi.model.PODashboard;
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

}
