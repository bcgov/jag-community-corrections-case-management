package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

@ApplicationScoped
public class UserDataServiceImpl implements UserDataService {

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Override
    public Code getDefaultLocation(String user) {

        Map location = obridgeClientService.getLocation();

        Code code = new Code();
        code.setKey(location.get("locationId").toString());
        code.setValue(location.get("locationText").toString());

        return code;

    }

}
