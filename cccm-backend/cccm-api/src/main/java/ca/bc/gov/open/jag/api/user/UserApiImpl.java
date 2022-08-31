package ca.bc.gov.open.jag.api.user;

import ca.bc.gov.open.jag.api.service.UserDataServiceImpl;
import ca.bc.gov.open.jag.cccm.api.openapi.UserApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
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

}
