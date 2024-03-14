package ca.bc.gov.open.jag.service;

import ca.bc.gov.open.jag.Keys;
import ca.bc.gov.open.jag.model.IdirUser;
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

import jakarta.enterprise.context.ApplicationScoped;
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

    private final String env;

    private final LdapService ldapService;

    public RoleSyncServiceImpl(@RestClient CCCMApiService cccmApiService, Keycloak keycloak, @ConfigProperty(name = "quarkus.keycloak.admin-client.realm") String realm, @ConfigProperty(name = "feature.user.role.removal") Boolean removeRole, @ConfigProperty(name = "cccm.env") String env, LdapService ldapService) {
        this.cccmApiService = cccmApiService;
        this.keycloak = keycloak;
        this.realm = realm;
        this.removeRole = removeRole;
        this.env = env;
        this.ldapService = ldapService;
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
        for (User user: dbUsers.stream().filter(user -> StringUtils.isNoneBlank(user.getIdirId())).collect(Collectors.toList())) {
            logger.info("processing user {}", user.getIdirId());
            users.removeIf(remUser -> remUser.getUsername().equals(StringUtils.lowerCase(user.getIdirId())));

            Optional<UserRepresentation> keyCloakUser = keycloak.realm(realm)
                    .users()
                    .searchByUsername(StringUtils.lowerCase(user.getIdirId()), true)
                    .stream()
                    .findFirst();

            Optional<UserResource> keyCloakUserResource = (keyCloakUser.isPresent() ? Optional.of(keycloak.realm(realm).users().get(keyCloakUser.get().getId())) : Optional.empty());
            if (keyCloakUser.isPresent() && keyCloakUserResource.get().groups().stream().noneMatch(innerGroup -> innerGroup.getName().equals(processingGroup))) {
                //Add user to role
                logger.info("adding user {} to group {}", user.getIdirId(), representation.getName());
                keyCloakUserResource.get().joinGroup(representation.getId());
                keyCloakUser.get().setEnabled(true);
                setOracleId(user, keyCloakUser.get());
                List<FederatedIdentityRepresentation> usersIdp = keycloak.realm(realm).users().get(keyCloakUser.get().getId()).getFederatedIdentity();
                if (usersIdp == null || usersIdp.isEmpty()) {
                    IdirUser idirUser = getIdirUser(user);
                    if (idirUser != null) {
                        keyCloakUser.get().setFirstName(idirUser.getFirstName());
                        keyCloakUser.get().setLastName(idirUser.getLastName());
                        keyCloakUser.get().setEmail(idirUser.getEmail());
                        keycloak.realm(realm).users().get(keyCloakUser.get().getId()).addFederatedIdentity("idir", getFederationLink(idirUser.getGuid()).get(0));
                    }
                }
                keycloak.realm(realm).users().get(keyCloakUser.get().getId()).update(keyCloakUser.get());
            } else if (keyCloakUser.isPresent()) {

                List<FederatedIdentityRepresentation> usersIdp = keycloak.realm(realm).users().get(keyCloakUser.get().getId()).getFederatedIdentity();
                if (usersIdp == null || usersIdp.isEmpty()) {
                    logger.info("Mapping user with missing idp");
                    IdirUser idirUser = getIdirUser(user);
                    if (idirUser != null) {
                        keyCloakUser.get().setFirstName(idirUser.getFirstName());
                        keyCloakUser.get().setLastName(idirUser.getLastName());
                        keyCloakUser.get().setEmail(idirUser.getEmail());
                        keycloak.realm(realm).users().get(keyCloakUser.get().getId()).update(keyCloakUser.get());

                        keycloak.realm(realm).users().get(keyCloakUser.get().getId()).addFederatedIdentity("idir", getFederationLink(idirUser.getGuid()).get(0));
                    }
                }
                setOracleId(user, keyCloakUser.get());

                keycloak.realm(realm).users().get(keyCloakUser.get().getId()).update(keyCloakUser.get());
                
            } else if (keyCloakUser.isEmpty()) {
                //Add user
                logger.info("creating user {} and adding to group {}", user.getIdirId(), representation.getName());
                UserRepresentation newUser = new UserRepresentation();
                newUser.setUsername(user.getIdirId());
                IdirUser idirUser = getIdirUser(user);
                if (idirUser != null) {
                    newUser.setFirstName(idirUser.getFirstName());
                    newUser.setLastName(idirUser.getLastName());
                    newUser.setEmail(idirUser.getEmail());
                    newUser.setFederatedIdentities(getFederationLink(idirUser.getGuid()));
                } else {
                    logger.warn("idirUser not present federation cannot be pre-set {}", user.getIdirId());
                }
                newUser.setEnabled(true);
                newUser.setGroups(Collections.singletonList(processingGroup));
                setOracleId(user, newUser);
                keycloak.realm(realm).users().create(newUser);
            }
        }
        //Any user left should be removed from group
        logger.info("Role feature is {}. # of user to remove {}", removeRole , users.size());
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
            case RESEARCHER:
                role = Keys.RESEARCHER;
                break;
            case ITRP:
                role = Keys.ITRP;
                break;
            case ADMINCOMM:
                role = Keys.ADMINCOMM;
                break;
            default:
                role = null;
                break;
        }
        return role;
    }

    private List<FederatedIdentityRepresentation> getFederationLink(String idirGuid) {

        String formattedGuid = formatGuid(idirGuid);

        FederatedIdentityRepresentation idirLink = new FederatedIdentityRepresentation();
        idirLink.setIdentityProvider(IDIR_IDP);
        idirLink.setUserId(formattedGuid);
        idirLink.setUserName(formattedGuid);

        return Collections.singletonList(idirLink);

    }

    private IdirUser getIdirUser(User user) {

        if (user.getIdirId() == null) return null;

        try {

            return ldapService.getUserByUsername(user.getIdirId());

        } catch (Exception e) {
            logger.error("Error getting idir information {}", e.getMessage());
            return null;
        }
    }

    private String formatGuid(String guid) {
        return guid.toLowerCase().concat("@idir");
    }

    private void setOracleId(User user, UserRepresentation keyCloakUser) {

        if (StringUtils.isBlank(user.getOracleId())) {
            logger.warn("User {} does not have an oracle id", user.getIdirId());
            return;
        }

        if (keyCloakUser.getAttributes() != null) {

            if (keyCloakUser.getAttributes().get(ORACLE_ID).stream().anyMatch(oracleId -> !oracleId.equals(user.getOracleId()))) {
                //Override existing oracle id if changed
                keyCloakUser.getAttributes().put(ORACLE_ID, Collections.singletonList(user.getOracleId()));
            } else {
                //Set oracle id
                keyCloakUser.getAttributes().putIfAbsent(ORACLE_ID, Collections.singletonList(user.getOracleId()));
            }

        } else {
            keyCloakUser.setAttributes(new HashMap<String, List<String>>() {{
                put(ORACLE_ID, Collections.singletonList(user.getOracleId()));
            }});
        }

    }

}
