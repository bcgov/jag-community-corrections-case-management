package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.mapper.LocationMapper;
import ca.bc.gov.open.jag.api.mapper.UserMapper;
import ca.bc.gov.open.jag.api.model.data.Location;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Code;
import ca.bc.gov.open.jag.cccm.api.openapi.model.CodeList;
import ca.bc.gov.open.jag.cccm.api.openapi.model.PODashboard;
import ca.bc.gov.open.jag.cccm.api.openapi.model.SupervisorDashboard;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static ca.bc.gov.open.jag.api.Keys.USERNAME_PREFIX;
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

        Location location = obridgeClientService.getLocation(oracleId);

        Code code = new Code();
        code.setKey(location.getId().toPlainString());
        code.setValue(location.getDsc());

        return code;

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
    public List<PODashboard> getPODashboard(String user, String location) {
        return userMapper.toPODashboardList(obridgeClientService.getPODashboard(user.replace(USERNAME_PREFIX, ""), new BigDecimal(location)));
    }

    @Override
    public List<SupervisorDashboard> getSupervisorDashboard(String user, BigDecimal location) {
        return userMapper.toSupervisorDashboardList(obridgeClientService.getSupervisorDashboard(stripUserName(user),location));
    }
}
