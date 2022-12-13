package ca.bc.gov.open.jag.service;

import ca.bc.gov.open.jag.Keys;
import ca.bc.gov.open.jag.model.RoleGroupEnum;
import ca.bc.gov.open.jag.model.User;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.bc.gov.open.jag.Keys.IDIR_IDP;
import static ca.bc.gov.open.jag.Keys.ORACLE_ID;

@ApplicationScoped
public class RoleSyncServiceImpl implements RoleSyncService {

    private static final Logger logger = LoggerFactory.getLogger(RoleSyncServiceImpl.class);

    private final CCCMApiService cccmApiService;

    private final Keycloak keycloak;

    private final String realm;

    private final Boolean removeRole;

    public RoleSyncServiceImpl(@RestClient CCCMApiService cccmApiService, Keycloak keycloak, @ConfigProperty(name = "quarkus.keycloak.admin-client.realm") String realm, @ConfigProperty(name = "feature.user.role.removal") Boolean removeRole) {
        this.cccmApiService = cccmApiService;
        this.keycloak = keycloak;
        this.realm = realm;
        this.removeRole = removeRole;
    }

    @Override
    public void syncRoles() {

        for (RoleGroupEnum roleGroup: RoleGroupEnum.values()) {
            //We need to get the keycloak users every time. If a user is added we want the newly created user not to attempt to create
            logger.info("processing role group {}", roleGroup);
            processRoleGroup(cccmApiService.getUsers(roleGroup), roleGroup);
        }

    }

    private void processRoleGroup(List<User> dbUsers, RoleGroupEnum roleGroup) {
        String processingGroup = roleGroupEnumToKeycloakGroup(roleGroup);
        GroupRepresentation representation = keycloak.realm(realm).groups().groups().stream().filter(groupRepresentation -> groupRepresentation.getName().equals(processingGroup)).findFirst().get();
        List<UserRepresentation> users = keycloak.realm(realm).users().list().stream().filter(user -> userInGroup(user, processingGroup)).collect(Collectors.toList());
        for (User user: dbUsers) {
            logger.info("processing user {}", user.getIdirId());
            users.removeIf(remUser -> remUser.getUsername().equals(StringUtils.lowerCase(user.getIdirId())));
            Optional<UserRepresentation> keyCloakUser = keycloak.realm(realm).users().search(StringUtils.lowerCase(user.getIdirId())).stream().findFirst();
            Optional<UserResource> keyCloakUserResource = (keyCloakUser.isPresent() ? Optional.of(keycloak.realm(realm).users().get(keyCloakUser.get().getId())) : Optional.empty());
            if (keyCloakUser.isPresent() && keyCloakUserResource.get().groups().stream().noneMatch(innerGroup -> innerGroup.getName().equals(processingGroup))) {
                //Add user to role
                logger.info("adding user {} to group {}", user.getIdirId(), representation.getName());
                keyCloakUserResource.get().joinGroup(representation.getId());
                keyCloakUser.get().setEnabled(true);
                if (keyCloakUser.get().getAttributes() != null) {
                    keyCloakUser.get().getAttributes().putIfAbsent(ORACLE_ID, Collections.singletonList(user.getOracleId()));
                } else {
                    keyCloakUser.get().setAttributes(new HashMap<String, List<String>>() {{ put(ORACLE_ID, Collections.singletonList(user.getOracleId())); }});
                }
                keycloak.realm(realm).users().get(keyCloakUser.get().getId()).update(keyCloakUser.get());
            } else if (keyCloakUser.isEmpty()) {
                //Add user
                logger.info("creating user {} and adding to group {}", user.getIdirId(), representation.getName());
                UserRepresentation newUser = new UserRepresentation();
                newUser.setUsername(user.getIdirId());
                newUser.setFederatedIdentities(getFederationLink(user.getIdirId()));
                newUser.setAttributes(new HashMap<String, List<String>>() {{ put(ORACLE_ID, Collections.singletonList(user.getOracleId())); }});
                newUser.setGroups(Collections.singletonList(processingGroup));
                keycloak.realm(realm).users().create(newUser);
            }
        }
        //Any user left should be removed from group
        if (removeRole) {
            for (UserRepresentation user : users) {
                logger.info("removing user {} from group {}", user.getUsername(), representation.getName());
                UserResource keyCloakUserResource = keycloak.realm(realm).users().get(user.getId());
                keyCloakUserResource.leaveGroup(representation.getId());
                //Disable user if there are no groups
                if (keyCloakUserResource.groups().isEmpty()) {
                    logger.info("user {} will be disabled", user.getUsername());
                    user.setEnabled(false);
                }
                keycloak.realm(realm).users().get(user.getId()).update(user);
            }
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

    private List<FederatedIdentityRepresentation> getFederationLink(String idirId) {

        FederatedIdentityRepresentation idirLink = new FederatedIdentityRepresentation();
        //TODO: this requires user maping and endpoint to get details
        idirLink.setIdentityProvider(IDIR_IDP);
        idirLink.setUserId("");
        idirLink.setUserName("");

        return Collections.singletonList(idirLink);

    }

}
