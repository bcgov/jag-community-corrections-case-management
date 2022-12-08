package ca.bc.gov.open.jag.service;

import ca.bc.gov.open.jag.config.RequestUUIDHeaderFactory;
import ca.bc.gov.open.jag.model.RoleGroupEnum;
import ca.bc.gov.open.jag.model.User;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@RequestScoped
@RegisterClientHeaders(RequestUUIDHeaderFactory.class)
@RegisterRestClient
public interface CCCMApiService {

    @GET
    @Path("/role/users")
    List<User> getUsers(@QueryParam("group") RoleGroupEnum group);

}
