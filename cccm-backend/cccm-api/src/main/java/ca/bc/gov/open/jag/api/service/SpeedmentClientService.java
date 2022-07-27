package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.Address;
import ca.bc.gov.open.jag.api.model.Alert;
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
    @Path("/CMS/getClientAddress")
    List<Address> getClientAddress(@QueryParam("clientNo") String clientNo);

    @GET
    @Path("/CMS/getClientId")
    BigDecimal getClientId(@QueryParam("clientNo") String clientNo);

    @GET
    @Path("/CMS/getAlertsByClient")
    List<Alert> getAlerts(@QueryParam("clientId") BigDecimal clientId);


}
