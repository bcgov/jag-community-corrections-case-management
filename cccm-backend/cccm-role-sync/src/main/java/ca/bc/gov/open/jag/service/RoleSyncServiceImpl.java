package ca.bc.gov.open.jag.service;

import ca.bc.gov.open.jag.Keys;
import ca.bc.gov.open.jag.model.RoleGroupEnum;
import ca.bc.gov.open.jag.model.User;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
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
        GroupRepresentation representation = keycloak.realm(realm).groups().groups().stream().filter(groupRepresentation -> groupRepresentation.getName().equals(processingGroup)).findFirst().get();
        List<UserRepresentation> users = keycloak.realm(realm).users().list().stream().filter(user -> userInGroup(user, processingGroup)).collect(Collectors.toList());
        for (User user: dbUsers) {
            users.removeIf(remUser -> remUser.getUsername().equals(StringUtils.lowerCase(user.getIdirId())));
            Optional<UserRepresentation> keyCloakUser = keycloak.realm(realm).users().search(StringUtils.lowerCase(user.getIdirId())).stream().findFirst();
            Optional<UserResource> keyCloakUserResource = (keyCloakUser.isPresent() ? Optional.of(keycloak.realm(realm).users().get(keyCloakUser.get().getId())) : Optional.empty());
            if (keyCloakUser.isPresent() && keyCloakUserResource.get().groups().stream().noneMatch(innerGroup -> innerGroup.getName().equals(processingGroup))) {
                //Add user to role
                keyCloakUserResource.get().joinGroup(representation.getId());
                keyCloakUser.get().setEnabled(true);
                keycloak.realm(realm).users().get(keyCloakUser.get().getId()).update(keyCloakUser.get());
            } else if (keyCloakUser.isEmpty()) {
                //Add user
                UserRepresentation newUser = new UserRepresentation();
                newUser.setUsername(user.getIdirId());
                newUser.setGroups(Collections.singletonList(processingGroup));
                keycloak.realm(realm).users().create(newUser);
            }
        }
        //Any user left should be removed from group
        for (UserRepresentation user: users) {
            UserResource keyCloakUserResource = keycloak.realm(realm).users().get(user.getId());
            keyCloakUserResource.leaveGroup(representation.getId());
            //Disable user if there are no groups
            if (keyCloakUserResource.groups().isEmpty()) {
                user.setEnabled(false);
            }
            keycloak.realm(realm).users().get(user.getId()).update(user);
        }

    }

    private Boolean userInGroup(UserRepresentation user, String group) {
        List<GroupRepresentation> groups = keycloak.realm(realm).users().get(user.getId()).groups();
        return groups.stream().anyMatch(currentGroup -> currentGroup.getName().equals(group));
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

}
