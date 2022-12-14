package ca.bc.gov.open.jag.service;

import ca.bc.gov.open.jag.config.SSORequestUUIDHeaderFactory;
import ca.bc.gov.open.jag.model.Data;
import ca.bc.gov.open.jag.model.IdirUser;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@RequestScoped
@RegisterClientHeaders(SSORequestUUIDHeaderFactory.class)
@RegisterRestClient
public interface CSSSSOApiService {

    @GET
    @Path("/{env}/idir/users")
    Data getIdirUsers(@PathParam("env") String env, @QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName);

}
