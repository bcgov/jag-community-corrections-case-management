package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.Keys;
import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import ca.bc.gov.open.jag.api.mapper.LocationMapper;
import ca.bc.gov.open.jag.api.mapper.UserMapper;
import ca.bc.gov.open.jag.api.model.data.Location;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static ca.bc.gov.open.jag.api.Keys.*;
import static ca.bc.gov.open.jag.api.util.JwtUtils.stripUserName;

@RequestScoped
public class UserDataServiceImpl implements UserDataService {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(UserDataServiceImpl.class));

    private final ObridgeClientService obridgeClientService;

    private final LocationMapper locationMapper;

    private final UserMapper userMapper;

    public UserDataServiceImpl(@RestClient ObridgeClientService obridgeClientService, LocationMapper locationMapper, UserMapper userMapper) {
        this.obridgeClientService = obridgeClientService;
        this.locationMapper = locationMapper;
        this.userMapper = userMapper;
    }

    @Override
    public LocationCode getDefaultLocation(String user) {

        logger.debug("Default Location {}", user);

        logger.info("Getting Oracle Id For Default Location");
        String oracleId = obridgeClientService.getOracleId(stripUserName(user));

        logger.info("Getting Location Code For Default Location");
        return getCode(oracleId);

    }

    @Override
    public LocationCodeList getLocations(String user) {

        logger.debug("Default Locations {}", user);

        logger.info("Getting Oracle Id For Default Locations");
        String oracleId = obridgeClientService.getOracleId(stripUserName(user));

        logger.info("Getting Locations Code For Default Locations");
        List<Location> locations = obridgeClientService.getLocations(oracleId);

        return locationMapper.toLocationCodeResult("", locations);
    }

    @Override
    public String getOracleId(String user) {

        logger.debug("Oracle Id {}", user);

        return obridgeClientService.getOracleId(stripUserName(user));

    }

    @Override
    public List<PODashboard> getPODashboard(String user, BigDecimal location) {

        logger.debug("PO Dashboard user {} location {}", user, location);
		if(user != null) {
        	user = user.replace(USERNAME_PREFIX, "");
        }

        return userMapper.toPODashboardList(obridgeClientService.getPODashboard(user, location));

    }

    @Override
    public List<CentreDashboard> getCentreDashboard(BigDecimal location) {

        logger.debug("Centre Dashboard location {}", location);

        return obridgeClientService.getCentreDashboard(location)
                .stream()
                .map(userMapper::toCentreDashboard)
                .collect(Collectors.toList());

    }

    @Override
    public List<SupervisorDashboard> getSupervisorDashboard(String user, BigDecimal location) {

        logger.debug("Supervisor Dashboard user {} location {}", user, location);

        return userMapper.toSupervisorDashboardList(obridgeClientService.getSupervisorDashboard(stripUserName(user),location));

    }

    @Override
    public SupervisorDashboardDetails getSupervisorDashboardDetails(String userId, BigDecimal location) {

        logger.debug("Supervisor Dashboard Details user {} location {}", userId, location);

        return userMapper.toSupervisorDashboardDetails(obridgeClientService.getSupervisorDashboardDetails(userId, location));

    }

    @Override
    public LogonResult logonUser(String user, String locationType) {

        logger.debug("Logon User user {} location {}", user, locationType);

        LogonResult logonResult = new LogonResult();

        String idirId = stripUserName(user);

        logger.info("Set Login Date");
        obridgeClientService.setLoginDate(idirId);
        logger.info("Get Oracle Id");
        String oracleId = obridgeClientService.getOracleId(idirId);
        logger.info("Get Default Location");
        logonResult.setDefaultLocation(getCode(oracleId));
        logger.info("Get Locations");
        logonResult.setLocations(locationMapper.toCodeResult("", refinedLocationsByType(oracleId, locationType)));

        return logonResult;
    }

    @Override
    public List<PO> getPOList(String user, BigDecimal location) {

        logger.debug("Get po user list {} location {}", user, location);

        return userMapper.toPOList(obridgeClientService.getPOUsers(location));

    }

    private LocationCode getCode(String oracleId) {

        Location location = obridgeClientService.getLocation(oracleId);

        LocationCode code = new LocationCode();
        code.setKey(location.getId().toPlainString());
        code.setValue(location.getDsc());
        code.setLocationTypeCode(location.getlotyCd());

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
