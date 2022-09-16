package ca.bc.gov.open.jag.api.user;

import ca.bc.gov.open.jag.api.service.UserDataServiceImpl;
import ca.bc.gov.open.jag.cccm.api.openapi.UserApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.keycloak.KeycloakPrincipal;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Logger;

@RequestScoped
public class UserApiImpl implements UserApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(UserApiImpl.class));

    @Inject
    UserDataServiceImpl userDataService;

    @Inject
    @Claim(standard = Claims.preferred_username)
    String username;

    @Override
    @RolesAllowed("data-view")
    public Code getUserDefaultLocation() {

        logger.info("Default Location Request");

        return userDataService.getDefaultLocation(username);

    }

    @Override
    @RolesAllowed("data-view")
    public String getUserId() {
        return userDataService.getOracleId(username);
    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getUserLocations() {
        return userDataService.getLocations(username);
    }

}
