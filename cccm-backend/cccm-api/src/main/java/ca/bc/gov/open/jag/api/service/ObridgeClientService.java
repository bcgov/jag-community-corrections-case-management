package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.Client;
import ca.bc.gov.open.jag.api.model.data.PODashboard;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.model.data.*;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import java.math.BigDecimal;
import java.util.List;

@RequestScoped
@RegisterClientHeaders
@RegisterRestClient
public interface ObridgeClientService {

    //TODO: user and location should be moved to the header for all obridge requests
    @GET
    @Path("/clientSearch")
    List<Client> getClientSearch(@QueryParam("searchType") String searchType,
                                 @QueryParam("surname") String surname,
                                 @QueryParam("givenName") String givenName,
                                 @QueryParam("birthYear") BigDecimal birthYear,
                                 @QueryParam("birthYearRange") BigDecimal birthYearRange,
                                 @QueryParam("age") Integer age,
                                 @QueryParam("gender") String gender,
                                 @QueryParam("identifierType") String identifierType,
                                 @QueryParam("identifierText") String identifierText);

    @GET
    @Path("/clientAddressSearch")
    List<Client> getClientAddressSearch(@QueryParam("addressType") String addressType,
                                        @QueryParam("address") String address,
                                        @QueryParam("city") String city,
                                        @QueryParam("province") String province,
                                        @QueryParam("postalCode") String postalCode,
                                        @QueryParam("expired") Boolean expired);

    @GET
    @Path("/clientSearch")
        //Lastname search only
    List<Client> getClientSearch(@QueryParam("searchType") String searchType,
                                 @QueryParam("surname") String surname);

    @GET
    @Path("/clientSearch")
        //Get a client
    List<Client> getClientById(@QueryParam("searchType") String searchType,
                               @QueryParam("identifierType") String identifierType,
                               @QueryParam("identifierText") String identifierText);

    @GET
    @Path("/client/{clientNum}/photo")
    List<Photo> getPhotosById(@PathParam("clientNum") String clientNum);

    @GET
    @Path("/client/address")
    List<ca.bc.gov.open.jag.api.model.data.Address> getAddressById(@QueryParam("clientNum") String clientNum, @QueryParam("user") String user, @QueryParam("location") BigDecimal location);

    @GET
    @Path("/client/{clientNum}/details")
    Client getDetailsById(@PathParam("clientNum") String clientNum, @QueryParam("user") String user, @QueryParam("location") BigDecimal location);

    @GET
    @Path("/client/clientProfile")
    ClientProfile getProfileById(@QueryParam("csNumber") String csNumber,
                                 @QueryParam("user") String user,
                                 @QueryParam("location") BigDecimal location);

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
    @Path("/lookup/intervention-types")
    List<CodeTable> getInterventionTypes();

    @GET
    @Path("/lookup/responsivity-types")
    List<CodeTable> getResponsivityTypes();

    @GET
    @Path("/user/location")
    Location getLocation(@QueryParam("oracleId") String oracleId);

    @GET
    @Path("/user/locations")
    List<Location> getLocations(@QueryParam("oracleId") String oracleId);

    @GET
    @Path("/user/oracleId")
    String getOracleId(@QueryParam("idirId") String idirId);

    @GET
    @Path("/user/dashboard/po")
    List<PODashboard> getPODashboard(@QueryParam("idirId") String idirId, @QueryParam("locationId") BigDecimal locationId);

    @GET
    @Path("/forms/client/json/{clientNumber}/{clientFormId}")
    String getClientFormAsJSON(@PathParam("clientNumber") String clientNumber,
                               @PathParam("clientFormId") BigDecimal clientFormId,
                               @QueryParam("includeValues") boolean includeValues);

    @POST
    @Path("/forms/client")
    BigDecimal createForm(@RequestBody CreateFormInput createFormInput);

    @PUT
    @Path("/forms/client/answers/{clientNumber}/{clientFormId}")
    String saveClientFormAnswers(@PathParam("clientNumber") String clientNumber,
                                     @PathParam("clientFormId") BigDecimal clientFormId,
                                     @RequestBody String payload,
                                     @QueryParam("loadLatestValues") boolean loadLatestValues);

    @PUT
    @Path("/forms/client/answers/interventions/{clientNumber}/{clientFormId}")
    String updateClientFormInterventions(@PathParam("clientNumber") String clientNumber,
                                 @PathParam("clientFormId") BigDecimal clientFormId,
                                 @RequestBody String payload);

    @GET
    @Path("/forms/client/answers/{clientNumber}/{clientFormId}")
    String getClientFormAnswers(@PathParam("clientNumber") String clientNumber,
                                           @PathParam("clientFormId") BigDecimal clientFormId);


    @GET
    @Path("forms/client/summary/answers/{clientNumber}/{clientFormId}")
    String getClientFormAnswersSummary(@PathParam("clientNumber") String clientNumber,
                                @PathParam("clientFormId") BigDecimal clientFormId);

    @GET
    @Path("forms/client/summary/{clientNumber}/{clientFormId}")
    String getClientFormSummary(@PathParam("clientNumber") String clientNumber,
                                       @PathParam("clientFormId") BigDecimal clientFormId);


    @GET
    @Path("/forms/client/answers/{clientNumber}/{clientFormId}/{sectionSequence}")
    String getClientFormAnswersForSection(@PathParam("clientNumber") String clientNumber,
                                                @PathParam("clientFormId") BigDecimal clientFormId,
                                                @PathParam("sectionSequence") int sectionSequence);

    @GET
    @Path("/forms/client/answers/{clientNumber}/{clientFormId}/{sectionSequence}/{questionSequence}")
    String getClientFormAnswersForSectionAndQuestion(@PathParam("clientNumber") String clientNumber,
                                                     @PathParam("clientFormId") BigDecimal clientFormId,
                                                     @PathParam("sectionSequence") int sectionSequence,
                                                     @PathParam("questionSequence") int questionSequence);

    @GET
    @Path("/forms/client/search/{clientId}")
    List<ClientFormSummary> getClientForms(@PathParam("clientId") String clientId,
                                           @QueryParam("currentPeriod") boolean currentPeriod,
                                           @QueryParam("formTypeCd") String formTypeCd);



    @POST
    @Path("/forms/client/interventions/{csNumber}")
    List<Intervention> searchClientInterventions(@PathParam("csNumber") String clientNumber, @RequestBody ClientSearchInput searchInput);

    @POST
    @Path("/forms/client/comments/{csNumber}")
    List<Comment> searchClientComments(@PathParam("csNumber") String clientNumber, @RequestBody ClientSearchInput searchInput);

    @POST
    @Path("/forms/client/responsivities/{csNumber}")
    List<Responsivity> searchClientResponsivities(@PathParam("csNumber") String csNumber, @RequestBody ClientSearchInput searchInput);


    @GET
    @Path("/trend/client/{csNumber}/{reportType}/factors")
    List<LabelValuePair> getClientFormFactors(@PathParam("reportType") String reportType, @PathParam("csNumber") String csNumber);



    @GET
    @Path("/forms/{formId}")
    FormDetails getForm(@PathParam("formId") BigDecimal formId,
                        @QueryParam("includeAnswers") boolean includeAnswers);


    @GET
    @Path("/forms/decorator/{identifier}")
    String getFormDecorator(@PathParam("identifier") String identifier);

    @GET
    @Path("/forms/summaries")
    List<FormSummary> getFormSummaries(@QueryParam("module") String module,
                                       @QueryParam("latestOnly") boolean latestOnly);


    @GET
    @Path("/trend/types")
    List<TrendAnalysisConfig> getTrendTypes();

    @GET
    @Path("/trend/{type}/factors")
    List<LabelValuePair> getTrendFactors(@PathParam("type") String type);

    @POST
    @Path("/trend/client/data")
    TrendFormData getClientTrendData(@RequestBody TrendFilterInput input);
}
