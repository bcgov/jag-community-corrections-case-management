package ca.bc.gov.open.jag.api.role.service;

import ca.bc.gov.open.jag.cccm.api.openapi.model.User;

import java.util.List;

public interface RoleService {

    List<User> userRoles(String group);

}
