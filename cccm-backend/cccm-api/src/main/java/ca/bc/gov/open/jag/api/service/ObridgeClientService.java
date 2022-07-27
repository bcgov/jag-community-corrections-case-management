package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.Client;
import ca.bc.gov.open.jag.api.model.ClientProfile;
import ca.bc.gov.open.jag.api.model.Photo;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
@RegisterRestClient
public interface ObridgeClientService {

    @GET
    @Path("/clientSearch")
    //TODO: Add optional query params, for poc using only basic
    List<Client> getClientSearch(@QueryParam("searchType") String searchType, @QueryParam("surname") String surname);

    @GET
    @Path("/clientSearch")
    //TODO: Add optional query params, for poc using only basic
    List<Client> getClientById(@QueryParam("searchType") String searchType, @QueryParam("identifierType") String identifierType,
                               @QueryParam("identifierText") String identifierText);

    @GET
    @Path("/client/{clientId}/photo")
    List<Photo> getPhotosById(@PathParam("clientId") BigDecimal clientId);

    @GET
    @Path("/client/{clientId}/information")
    ClientProfile getProfileById(@PathParam("clientId") BigDecimal clientId);

}
