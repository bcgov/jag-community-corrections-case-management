package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.model.Client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.util.List;

@Path("/clients")
public interface ClientsApi {

    @GET
    @Produces({ "application/json" })
    List<Client> clientSearch(@QueryParam("name") String name, @QueryParam("soundex") Boolean soundex,
                              @QueryParam("birthYear") Integer birthYear, @QueryParam("age") Integer age,
                              @QueryParam("address") String address, @QueryParam("location") String location,
                              @QueryParam("gender") String gender, @QueryParam("clientId") BigDecimal clientId);

}
