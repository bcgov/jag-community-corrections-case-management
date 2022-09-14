package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.*;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CreateFormInput;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.model.FormSummary;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
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

    @GET
    @Path("/forms/client/json/{clientNumber}/{clientFormId}")
    String getClientFormAsJSON(@PathParam("clientNumber") String clientNumber, @PathParam("clientFormId") BigDecimal clientFormId, @QueryParam("includeValues") boolean includeValues);

    @POST
    @Path("/forms/client")
    BigDecimal createForm(CreateFormInput createFormInput);

    @GET
    @Path("/forms/client/{clientId}")
    List<Form> getClientForms(@PathParam("clientId") String clientId, @QueryParam("currentPeriod") boolean currentPeriod, @QueryParam("formTypeCd") String formTypeCd);

    @GET
    @Path("/forms/{formId}")
    FormDetails getForm(@PathParam("formId") BigDecimal formId, @QueryParam("includeAnswers") boolean includeAnswers);


    @GET
    @Path("/forms/summaries")
    List<FormSummary> getFormSummaries(@QueryParam("module") String module, @QueryParam("latestOnly") boolean latestOnly);


}
