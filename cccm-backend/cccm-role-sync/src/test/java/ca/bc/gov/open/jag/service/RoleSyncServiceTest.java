package ca.bc.gov.open.jag.service;

import ca.bc.gov.open.jag.Keys;
import ca.bc.gov.open.jag.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleSyncServiceTest {

    RoleSyncServiceImpl sut;

    @Mock
    CCCMApiService cccmApiServiceMock;

    @Mock
    Keycloak keycloakMock;

    @Mock
    RealmResource realmMock;

    @Mock
    GroupsResource groupsResourceMock;

    @Mock
    UsersResource usersResourceMock;

    @Mock
    LdapService ldapServiceMock;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);

        sut = new RoleSyncServiceImpl(cccmApiServiceMock, keycloakMock, "REALM", true, "test", ldapServiceMock);

    }

    @Test
    public void testProcessAll() {

        Mockito.when(cccmApiServiceMock.getUsers(any())).thenReturn(createUsers());
        Mockito.when(usersResourceMock.search(any())).thenReturn(new ArrayList<>());
        Mockito.when(usersResourceMock.list()).thenReturn(new ArrayList<>());
        Mockito.when(usersResourceMock.create(any())).thenReturn(null);
        Mockito.when(usersResourceMock.get(any())).thenReturn(new UserResource() {
            @Override
            public UserRepresentation toRepresentation() {
                return null;
            }

            @Override
            public UserRepresentation toRepresentation(boolean b) {
                return null;
            }

            @Override
            public void update(UserRepresentation userRepresentation) {

            }

            @Override
            public void remove() {

            }

            @Override
            public List<GroupRepresentation> groups() {
                return createGroups();
            }

            @Override
            public List<GroupRepresentation> groups(Integer integer, Integer integer1) {
                return null;
            }

            @Override
            public List<GroupRepresentation> groups(String s, Integer integer, Integer integer1) {
                return null;
            }

            @Override
            public List<GroupRepresentation> groups(Integer integer, Integer integer1, boolean b) {
                return null;
            }

            @Override
            public List<GroupRepresentation> groups(String s, boolean b) {
                return null;
            }

            @Override
            public List<GroupRepresentation> groups(String s, Integer integer, Integer integer1, boolean b) {
                return null;
            }

            @Override
            public Map<String, Long> groupsCount(String s) {
                return null;
            }

            @Override
            public void joinGroup(String s) {

            }

            @Override
            public void leaveGroup(String s) {

            }

            @Override
            public void logout() {

            }

            @Override
            public List<CredentialRepresentation> credentials() {
                return null;
            }

            @Override
            public List<String> getConfiguredUserStorageCredentialTypes() {
                return null;
            }

            @Override
            public void removeCredential(String s) {

            }

            @Override
            public void setCredentialUserLabel(String s, String s1) {

            }

            @Override
            public void moveCredentialToFirst(String s) {

            }

            @Override
            public void moveCredentialAfter(String s, String s1) {

            }

            @Override
            public void disableCredentialType(List<String> list) {

            }

            @Override
            public void resetPassword(CredentialRepresentation credentialRepresentation) {

            }

            @Override
            public void resetPasswordEmail() {

            }

            @Override
            public void resetPasswordEmail(String s) {

            }

            @Override
            public void executeActionsEmail(List<String> list) {

            }

            @Override
            public void executeActionsEmail(List<String> list, Integer integer) {

            }

            @Override
            public void executeActionsEmail(String s, String s1, Integer integer, List<String> list) {

            }

            @Override
            public void executeActionsEmail(String s, String s1, List<String> list) {

            }

            @Override
            public void sendVerifyEmail() {

            }

            @Override
            public void sendVerifyEmail(String s) {

            }

            @Override
            public List<UserSessionRepresentation> getUserSessions() {
                return null;
            }

            @Override
            public List<UserSessionRepresentation> getOfflineSessions(String s) {
                return null;
            }

            @Override
            public List<FederatedIdentityRepresentation> getFederatedIdentity() {
                return null;
            }

            @Override
            public Response addFederatedIdentity(String s, FederatedIdentityRepresentation federatedIdentityRepresentation) {
                return null;
            }

            @Override
            public void removeFederatedIdentity(String s) {

            }

            @Override
            public RoleMappingResource roles() {
                return null;
            }

            @Override
            public List<Map<String, Object>> getConsents() {
                return null;
            }

            @Override
            public void revokeConsent(String s) {

            }

            @Override
            public Map<String, Object> impersonate() {
                return null;
            }
        });
        Mockito.when(realmMock.users()).thenReturn(usersResourceMock);
        Mockito.when(realmMock.groups()).thenReturn(groupsResourceMock);
        Mockito.when(groupsResourceMock.groups()).thenReturn(createGroups());
        Mockito.when(keycloakMock.realm(any())).thenReturn(realmMock);

        Assertions.assertDoesNotThrow(() -> sut.syncRoles());


    }

    private List<GroupRepresentation> createGroups() {

        GroupRepresentation poGroup = new GroupRepresentation();
        poGroup.setName(Keys.PO);

        GroupRepresentation supervisorGroup = new GroupRepresentation();
        supervisorGroup.setName(Keys.SUPERVISOR);

        GroupRepresentation adminGroup = new GroupRepresentation();
        adminGroup.setName(Keys.ADMIN);

        GroupRepresentation researcherGroup = new GroupRepresentation();
        researcherGroup.setName(Keys.RESEARCHER);

        GroupRepresentation itrpGroup = new GroupRepresentation();
        itrpGroup.setName(Keys.ITRP);

        GroupRepresentation adminComm = new GroupRepresentation();
        adminComm.setName(Keys.ADMINCOMM);

        return Arrays.asList(poGroup, supervisorGroup, adminGroup, researcherGroup, itrpGroup, adminComm);

    }

    private List<User> createUsers() {

        User userOne = new User();
        userOne.setIdirId("TESTONE");
        userOne.setOracleId("TST");

        User userTwo = new User();
        userTwo.setIdirId("TESTTWO");

        User userThree = new User();
        userThree.setIdirId("TESTTHREWW");
        userOne.setOracleId("TSTTHREE");

        return Arrays.asList(userOne, userTwo, userThree);

    }

}
