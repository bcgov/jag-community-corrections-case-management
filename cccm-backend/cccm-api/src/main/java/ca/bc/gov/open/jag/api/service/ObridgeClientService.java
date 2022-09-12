package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.Client;
import ca.bc.gov.open.jag.api.model.data.ClientProfile;
import ca.bc.gov.open.jag.api.model.data.CodeTable;
import ca.bc.gov.open.jag.api.model.data.Photo;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@RegisterRestClient
public interface ObridgeClientService {

    @GET
    @Path("/clientSearch")
    List<Client> getClientSearch(@QueryParam("searchType") String searchType, @QueryParam("surname") String surname,
                                 @QueryParam("givenName") String givenName, @QueryParam("birthYear") BigDecimal birthYear,
                                 @QueryParam("birthYearRange") BigDecimal birthYearRange, @QueryParam("age") Integer age,
                                 @QueryParam("gender") String gender, @QueryParam("identifierType") String identifierType,
                                 @QueryParam("identifierText") String identifierText);

    @GET
    @Path("/clientAddressSearch")
    List<Client> getClientAddressSearch(@QueryParam("addressType") String addressType, @QueryParam("address") String address,
                                 @QueryParam("city") String city, @QueryParam("province") String province,
                                 @QueryParam("postalCode") String postalCode, @QueryParam("expired") Boolean expired);

    @GET
    @Path("/clientSearch")
    //Lastname search only
    List<Client> getClientSearch(@QueryParam("searchType") String searchType, @QueryParam("surname") String surname);

    @GET
    @Path("/clientSearch")
    //Get a client
    List<Client> getClientById(@QueryParam("searchType") String searchType, @QueryParam("identifierType") String identifierType,
                               @QueryParam("identifierText") String identifierText);

    @GET
    @Path("/client/{clientNum}/photo")
    List<Photo> getPhotosById(@PathParam("clientNum") String clientNum);

    @GET
    @Path("/client/clientProfile")
    ClientProfile getProfileById(@QueryParam("csNumber") String csNumber, @QueryParam("user") String user, @QueryParam("location") BigDecimal location);

    @GET
    @Path("/lookup/identifierTypes")
    List<CodeTable> getIdentifierTypes();

    @GET
    @Path("/lookup/addressTypes")
    List<CodeTable> getAddressTypes();

    @GET
    @Path("/lookup/genderTypes")
    List<CodeTable> getGenderTypes();

    @GET
    @Path("/location")
    Map getLocation();

}
