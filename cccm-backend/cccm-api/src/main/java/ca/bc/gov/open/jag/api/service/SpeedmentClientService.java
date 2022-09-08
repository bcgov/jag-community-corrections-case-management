package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.model.data.Form;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
@RegisterRestClient
public interface SpeedmentClientService {

    @GET
    @Path("/CMS/getClientId")
    BigDecimal getClientId(@QueryParam("clientNo") String clientNo);

    @GET
    @Path("/CMS/getFormsByClient")
    List<Form> getFormsByClient(@QueryParam("clientNo") String clientNo);

    @GET
    @Path("/CMS/getFormsByClient")
    List<Form> getFormsByClient(@QueryParam("clientNo") String clientNo, @QueryParam("formId") String formId);

    @GET
    @Path("/CMS/getLocation")
    List<CodeTable> getLocation();

    @GET
    @Path("/CMS/getFormTypes")
    List<CodeTable> getFormTypes();

}
