package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.data.CentreDashboard;
import ca.bc.gov.open.jag.api.model.data.Client;
import ca.bc.gov.open.jag.api.model.data.PODashboard;
import ca.bc.gov.open.jag.api.model.data.Photo;
import ca.bc.gov.open.jag.api.model.data.SupervisorDashboard;
import ca.bc.gov.open.jag.api.model.data.SupervisorDashboardDetails;
import ca.bc.gov.open.jag.api.model.data.User;
import ca.bc.gov.open.jag.api.model.data.*;
import ca.bc.gov.open.jag.api.model.service.DeleteRequest;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import java.math.BigDecimal;
import java.util.List;

@RequestScoped
@RegisterClientHeaders
@RegisterRestClient
public interface ObridgeClientService {

    // TODO: user and location should be moved to the header for all obridge
    // requests
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
            @QueryParam("identifierText") String identifierText,
            @QueryParam("user") String user,
            @QueryParam("location") BigDecimal location);

    @GET
    @Path("/clientAddressSearch")
    List<Client> getClientAddressSearch(@QueryParam("addressType") String addressType,
            @QueryParam("address") String address,
            @QueryParam("city") String city,
            @QueryParam("province") String province,
            @QueryParam("postalCode") String postalCode,
            @QueryParam("expired") Boolean expired,
            @QueryParam("user") String user,
            @QueryParam("location") BigDecimal location);

    @GET
    @Path("/clientSearch")
    // Lastname search only
    List<Client> getClientSearch(@QueryParam("searchType") String searchType,
            @QueryParam("surname") String surname);

    @GET
    @Path("/clientSearch")
    // Get a client
    List<Client> getClientById(@QueryParam("searchType") String searchType,
            @QueryParam("identifierType") String identifierType,
            @QueryParam("identifierText") String identifierText);

    @GET
    @Path("/client/{clientNumber}/photo")
    List<Photo> getPhotosById(@PathParam("clientNumber") String clientNum, @QueryParam("location") BigDecimal location);

    @GET
    @Path("/client/address")
    List<ca.bc.gov.open.jag.api.model.data.Address> getAddressById(@QueryParam("clientNumber") String clientNum,
            @QueryParam("user") String user, @QueryParam("location") BigDecimal location);

    @GET
    @Path("/client/{clientNumber}/details")
    Client getDetailsById(@PathParam("clientNumber") String clientNum, @QueryParam("user") String user,
            @QueryParam("location") BigDecimal location);

    @GET
    @Path("/client/clientProfile")
    ClientProfile getProfileById(@QueryParam("clientNumber") String csNumber,
            @QueryParam("user") String user,
            @QueryParam("location") BigDecimal location);

    @GET
    @Path("/client/{clientNumber}/client-dates")
    ClientDates getClientDates(@PathParam("clientNumber") String csNumber,
                                 @QueryParam("user") String user,
                                 @QueryParam("location") BigDecimal location);

    @GET
    @Path("/lookup/identifierTypes")
    List<CodeTable> getIdentifierTypes();

    @GET
    @Path("/lookup/addressTypes")
    List<CodeTable> getAddressTypes();

    @GET
    @Path("/lookup/provinceTypes")
    List<CodeTable> getProvinceTypes();

    @GET
    @Path("/lookup/cityTypes")
    List<CodeTable> getCityTypes();

    @GET
    @Path("/lookup/genderTypes")
    List<CodeTable> getGenderTypes();

    @GET
    @Path("/lookup/formTypes")
    List<CodeTable> getFormTypes(@QueryParam("formType") String formType);

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
    List<PODashboard> getPODashboard(@QueryParam("idirId") String idirId,
            @QueryParam("locationId") BigDecimal locationId);

    @GET
    @Path("/user/dashboard/centre")
    List<CentreDashboard> getCentreDashboard(@QueryParam("locationId") BigDecimal locationId);

    @GET
    @Path("/user/dashboard/supervisor")
    List<SupervisorDashboard> getSupervisorDashboard(@QueryParam("idirId") String idirId,
                                                     @QueryParam("locationId") BigDecimal locationId);

    @GET
    @Path("/user/dashboard/supervisor-details")
    SupervisorDashboardDetails getSupervisorDashboardDetails(@QueryParam("idirId") String idirId,
                                                             @QueryParam("locationId") BigDecimal locationId);

    @GET
    @Path("/forms/client/json/{clientNumber}/{clientFormId}")
    String getClientFormAsJSON(@PathParam("clientNumber") String clientNumber,
            @PathParam("clientFormId") BigDecimal clientFormId,
            @QueryParam("includeValues") boolean includeValues,
            @QueryParam("location") BigDecimal location);

    @POST
    @Path("/forms/client")
    BigDecimal createForm(@RequestBody FormInput createFormInput);

    @POST
    @Path("/forms/client/update/completion")
    void setCompletion(@RequestBody CompleteFormInput completeFormInput);

    @POST
    @Path("/forms/client/clone")
    BigDecimal cloneForm(@RequestBody CloneFormRequest cloneFormRequest);

    @PUT
    @Path("/forms/client/answers/{clientNumber}/{clientFormId}")
    String saveClientFormAnswers(@PathParam("clientNumber") String clientNumber,
            @PathParam("clientFormId") BigDecimal clientFormId,
            @RequestBody String payload,
			@QueryParam("skipAutoCalc") boolean skipAutoCalc,
            @QueryParam("loadLatestValues") boolean loadLatestValues,
            @QueryParam("location") BigDecimal location);

    @PUT
    @Path("/forms/client/answers/interventions/{clientNumber}/{clientFormId}")
    String updateClientFormInterventions(@PathParam("clientNumber") String clientNumber,
            @PathParam("clientFormId") BigDecimal clientFormId,
            @RequestBody String payload);

    @GET
    @Path("/forms/client/answers/{clientNumber}/{clientFormId}")
    String getClientFormAnswers(@PathParam("clientNumber") String clientNumber,
            @PathParam("clientFormId") BigDecimal clientFormId,
            @QueryParam("location") BigDecimal location);

    @GET
    @Path("/forms/client/summary/answers/{clientNumber}/{clientFormId}")
    String getClientFormAnswersSummary(@PathParam("clientNumber") String clientNumber,
            @PathParam("clientFormId") BigDecimal clientFormId,
            @QueryParam("includeLinkedForm") boolean includeLinkedForm,
            @QueryParam("location") BigDecimal location);

    @GET
    @Path("/forms/client/summary/{clientNumber}/{clientFormId}")
    ClientFormSummary getClientFormSummary(@PathParam("clientNumber") String clientNumber,
            @PathParam("clientFormId") BigDecimal clientFormId,
            @QueryParam("location") BigDecimal location);

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
            @QueryParam("formTypeCd") String formTypeCd,
            @QueryParam("location") BigDecimal location);

    @POST
    @Path("/forms/client/interventions")
    List<Intervention> searchClientInterventions(@RequestBody ClientSearchInput searchInput);

    @POST
    @Path("/forms/client/comments")
    List<Comment> searchClientComments(@RequestBody ClientSearchInput searchInput);

    @POST
    @Path("/forms/client/responsivities")
    List<Responsivity> searchClientResponsivities(@RequestBody ClientSearchInput searchInput);

    @GET
    @Path("/trend/client/{csNumber}/{reportType}/factors")
    List<LabelValuePair> getClientFormFactors(@PathParam("reportType") String reportType,
            @PathParam("csNumber") String csNumber);

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

    @GET
    @Path("/forms/client/intervention/json/{csNumber}/{clientFormId}")
    String getClientFormInterventionsForCasePlan(@PathParam("csNumber") String csNumber, @PathParam("clientFormId") BigDecimal clientFormId,
            @QueryParam("includeLinkedForm") boolean includeLinkedForm,
            @QueryParam("location") BigDecimal location);
    
    @GET
    @Path("/forms/client/meta/json/{csNumber}/{clientFormId}")
    String getClientFormMetaJson(@PathParam("csNumber") String csNumber,
                                 @PathParam("clientFormId") BigDecimal clientFormId,
                                 @QueryParam("location") BigDecimal location);
    
    @PUT
    @Path("/forms/client/sourcesContacted/{clientFormId}")
    String updateSourcesContacted(@PathParam("clientFormId") BigDecimal clientFormId, @RequestBody String sourcesContacted);
    
    @PUT
    @Path("/forms/client/{clientFormId}")
    String updateForm(@PathParam("clientFormId") BigDecimal clientFormId,
            @RequestBody String payload);

    @DELETE
    @Path("/forms/client/delete")
    String deleteForm(@RequestBody DeleteRequest deleteRequest);

    @PUT
    @Path("/user/{idirId}/login")
    void setLoginDate(@PathParam("idirId") String idirId);

    @GET
    @Path("/role/group-users")
    List<User> getUsers(@QueryParam("roleGroupEnum") String roleGroupEnum);

    @GET
    @Path("/user/po/list")
    List<User> getPOUsers(@QueryParam("locationId") BigDecimal location);

    @GET
    @Path("/forms/client/answers/{clientNumber}/{clientFormId}/object")
    ClientFormAnswers getClientFormAnswersObject(@PathParam("clientNumber") String clientNumber,
                                           @PathParam("clientFormId") BigDecimal clientFormId);

}
