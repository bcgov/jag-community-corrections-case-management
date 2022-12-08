package ca.bc.gov.open.jag.service;

import ca.bc.gov.open.jag.Keys;
import ca.bc.gov.open.jag.model.RoleGroupEnum;
import ca.bc.gov.open.jag.model.User;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoleSyncServiceImpl implements RoleSyncService {

    @Inject
    @RestClient
    CCCMApiService cccmApiService;

    @Inject
    Keycloak keycloak;

    @ConfigProperty(name = "quarkus.keycloak.admin-client.realm")
    String realm;

    @Override
    public void syncRoles() {

        for (RoleGroupEnum roleGroup: RoleGroupEnum.values()) {
            //We need to get the keycloak users every time. If a user is added we want the newly created user not to attempt to create
            processRoleGroup(cccmApiService.getUsers(roleGroup), roleGroup);
        }

    }

    private void processRoleGroup(List<User> dbUsers, RoleGroupEnum roleGroup) {
        String processingGroup = roleGroupEnumToKeycloakGroup(roleGroup);
        List<UserRepresentation> users = keycloak.realm(realm).users().list().stream().filter(user -> user.getGroups().contains(processingGroup)).collect(Collectors.toList());
        for (User user: dbUsers) {
            users.removeIf(remUser -> remUser.getUsername().equals(user.getIdirId()));
            Optional<UserRepresentation> keyCloakUser = keycloak.realm(realm).users().search(user.getIdirId()).stream().findFirst();
            if (keyCloakUser.isPresent() && !userInGroup(processingGroup, keyCloakUser.get())) {
                //Add user to role
                keyCloakUser.get().getGroups().add(processingGroup);
                keyCloakUser.get().setEnabled(true);
                keycloak.realm("").users().create(keyCloakUser.get());
            } else if (keyCloakUser.isEmpty()) {
                //Add user
                UserRepresentation newUser = new UserRepresentation();
                newUser.setUsername(user.getIdirId());
                newUser.setGroups(Collections.singletonList(processingGroup));
                keycloak.realm("").users().create(newUser);
            }
        }
        //Any user left should be removed from group
        for (UserRepresentation user: users) {
            user.getGroups().removeIf(role -> role.equals(processingGroup));
            //Disable user if there are no groups
            if (user.getGroups().isEmpty()) {
                user.setEnabled(false);
            }
            //keycloak.realm("").users().create(user);
        }

    }

    private String roleGroupEnumToKeycloakGroup(RoleGroupEnum roleGroup) {
        String role;
        switch (roleGroup) {
            case PO:
                role = Keys.PO;
                break;
            case SUPERVISOR:
                role = Keys.SUPERVISOR;
                break;
            case ADMIN:
                role = Keys.ADMIN;
                break;
            default:
                role = null;
                break;
        }
        return role;
    }

    private Boolean userInGroup(String group, UserRepresentation user) {
        return user.getGroups().stream().anyMatch(
                realmGroup -> realmGroup.equals(group)
        );

    }

}
