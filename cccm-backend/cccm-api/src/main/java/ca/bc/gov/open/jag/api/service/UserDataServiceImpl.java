package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.Keys;
import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.mapper.LocationMapper;
import ca.bc.gov.open.jag.api.mapper.UserMapper;
import ca.bc.gov.open.jag.api.model.data.Location;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static ca.bc.gov.open.jag.api.Keys.*;
import static ca.bc.gov.open.jag.api.util.JwtUtils.stripUserName;

@RequestScoped
public class UserDataServiceImpl implements UserDataService {

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    LocationMapper locationMapper;

    @Inject
    UserMapper userMapper;

    @Override
    public Code getDefaultLocation(String user) {

        String oracleId = obridgeClientService.getOracleId(stripUserName(user));

        return getCode(oracleId);

    }

    @Override
    public CodeList getLocations(String user) {

        String oracleId = obridgeClientService.getOracleId(stripUserName(user));

        List<Location> locations = obridgeClientService.getLocations(oracleId);

        return locationMapper.toCodeResult("", locations);
    }

    @Override
    public String getOracleId(String user) {
        return obridgeClientService.getOracleId(stripUserName(user));
    }

    @Override
    public List<PODashboard> getPODashboard(String user, BigDecimal location) {
        return userMapper.toPODashboardList(obridgeClientService.getPODashboard(user.replace(USERNAME_PREFIX, ""), location));
    }

    @Override
    public List<SupervisorDashboard> getSupervisorDashboard(String user, BigDecimal location) {
        return userMapper.toSupervisorDashboardList(obridgeClientService.getSupervisorDashboard(stripUserName(user),location));
    }

    @Override
    public LogonResult logonUser(String user, String locationType) {

        LogonResult logonResult = new LogonResult();

        String idirId = stripUserName(user);

        obridgeClientService.setLoginDate(idirId);

        String oracleId = obridgeClientService.getOracleId(idirId);

        logonResult.setDefaultLocation(getCode(oracleId));

        logonResult.setLocations(locationMapper.toCodeResult("", refinedLocationsByType(oracleId, locationType)));

        return logonResult;
    }

    private Code getCode(String oracleId) {

        Location location = obridgeClientService.getLocation(oracleId);

        Code code = new Code();
        code.setKey(location.getId().toPlainString());
        code.setValue(location.getDsc());

        return code;

    }

    private List<Location> refinedLocationsByType(String oracleId, String locationType) {

        List<Location> locations = obridgeClientService.getLocations(oracleId);

        List<Location> refinedLocations;

        switch (locationType) {
            case LOCATION_TYPE_ALL:
                refinedLocations = locations;
                break;
            case LOCATION_TYPE_COMMUNITY:
                refinedLocations = locations.stream()
                        .filter(
                        location -> location.getlotyCd().equalsIgnoreCase(Keys.CCCM_LOCATION)
                        )
                        .collect(Collectors.toList());
                break;
            default:
                throw new CCCMException("Location type unknown or not provided" , CCCMErrorCode.VALIDATIONERROR);
        }

        return refinedLocations;

    }

}
