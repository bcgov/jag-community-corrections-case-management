package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.Client;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@ApplicationScoped
@RegisterRestClient
public interface ObridgeClientService {

    @GET
    @Path("/clientSearch")
    //TODO: Add optional query params, for poc using only basic
    List<Client> getClientSearch(@QueryParam("searchType") String searchType, @QueryParam("surname") String surname);

}
