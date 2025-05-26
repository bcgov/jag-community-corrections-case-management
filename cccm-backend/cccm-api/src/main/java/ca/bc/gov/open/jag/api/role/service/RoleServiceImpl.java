package ca.bc.gov.open.jag.api.role.service;

import ca.bc.gov.open.jag.api.mapper.UserMapper;
import ca.bc.gov.open.jag.api.service.ObridgeClientService;
import ca.bc.gov.open.jag.cccm.api.openapi.model.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@RequestScoped
public class RoleServiceImpl implements RoleService {

    @Inject
    @RestClient
    ObridgeClientService obridgeClientService;

    @Inject
    UserMapper userMapper;

    @Override
    public List<User> userRoles(String group) {

        return userMapper.toUserList(obridgeClientService.getUsers(group));
        
    }

}
