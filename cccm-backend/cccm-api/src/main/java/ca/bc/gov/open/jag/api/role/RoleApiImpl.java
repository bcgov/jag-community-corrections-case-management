package ca.bc.gov.open.jag.api.role;

import ca.bc.gov.open.jag.api.role.service.RoleService;
import ca.bc.gov.open.jag.cccm.api.openapi.RoleApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequestScoped
@io.quarkus.arc.Unremovable
public class RoleApiImpl implements RoleApi {

    private static final Logger logger = LoggerFactory.getLogger(String.valueOf(RoleApi.class));


    private final RoleService roleService;

    @Inject
    public RoleApiImpl(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    @RolesAllowed("role-sync")
    public List<User> getUsers(@NotNull String group) {

        logger.info("Role Sync Users Request");

        return roleService.userRoles(group);

    }

}
