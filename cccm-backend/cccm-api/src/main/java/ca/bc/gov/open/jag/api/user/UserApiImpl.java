package ca.bc.gov.open.jag.api.user;

import ca.bc.gov.open.jag.api.service.FormDataService;
import ca.bc.gov.open.jag.api.service.UserDataServiceImpl;
import ca.bc.gov.open.jag.cccm.api.openapi.UserApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.LogonResult;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

@RequestScoped
public class UserApiImpl implements UserApi {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(UserApi.class));

    @Inject
    UserDataServiceImpl userDataService;

    @Inject
    @Claim(standard = Claims.preferred_username)
    String username;

    @Override
    public LogonResult getLogon(@NotNull String locationType) {

        logger.info("User Logon Request");

        return userDataService.logonUser(username, locationType);

    }

    @Override
    @RolesAllowed("data-view")
    public Code getUserDefaultLocation() {

        logger.info("Default Location Request");

        return userDataService.getDefaultLocation(username);

    }

    @Override
    @RolesAllowed("data-view")
    public String getUserId() {

        logger.info("Oracle Id Request");

        return userDataService.getOracleId(username);

    }

    @Override
    @RolesAllowed("data-view")
    public CodeList getUserLocations() {

        logger.info("User Locations Request");

        return userDataService.getLocations(username);

    }

}
