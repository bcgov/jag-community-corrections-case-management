package ca.bc.gov.open.jag.api.role;

import ca.bc.gov.open.jag.api.role.service.RoleService;
import ca.bc.gov.open.jag.cccm.api.openapi.RoleApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.User;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
@RequestScoped
public class RoleApiImpl implements RoleApi {


    @Inject
    RoleService roleService;

    @Override
    @RolesAllowed("role-sync")
    public List<User> getUsers(@NotNull String group) {
        return roleService.userRoles(group);
    }

}
