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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        UserResource userResourceMock = Mockito.mock(UserResource.class);
        Mockito.when(userResourceMock.groups()).thenReturn(createGroups());
        Mockito.when(usersResourceMock.get(any())).thenReturn(userResourceMock);
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

        GroupRepresentation auditQuery = new GroupRepresentation();
        auditQuery.setName(Keys.AUDITQUERY);

        GroupRepresentation po14 = new GroupRepresentation();
        po14.setName(Keys.PO14);

        GroupRepresentation po14Query = new GroupRepresentation();
        po14Query.setName(Keys.PO14QUERY);

        GroupRepresentation spo26 = new GroupRepresentation();
        spo26.setName(Keys.SPO26);

        GroupRepresentation lm = new GroupRepresentation();
        lm.setName(Keys.LM);

        GroupRepresentation rm = new GroupRepresentation();
        rm.setName(Keys.RM);

        GroupRepresentation corrections = new GroupRepresentation();
        corrections.setName(Keys.CORRECTIONS);

        GroupRepresentation ssu = new GroupRepresentation();
        ssu.setName(Keys.SSU);

        GroupRepresentation devmaint = new GroupRepresentation();
        devmaint.setName(Keys.DEVMAINT);

        GroupRepresentation jia = new GroupRepresentation();
        jia.setName(Keys.JIA);

        GroupRepresentation porvo = new GroupRepresentation();
        porvo.setName(Keys.PORVO);

        GroupRepresentation ssuaccess = new GroupRepresentation();
        ssuaccess.setName(Keys.SSUACCESS);

        GroupRepresentation sysadmin = new GroupRepresentation();
        sysadmin.setName(Keys.SYSADMIN);

        return Arrays.asList(
                poGroup,
                supervisorGroup,
                adminGroup,
                researcherGroup,
                itrpGroup,
                adminComm,
                auditQuery,
                po14,
                po14Query,
                spo26,
                lm,
                rm,
                corrections,
                ssu,
                devmaint,
                jia,
                porvo,
                ssuaccess,
                sysadmin);

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
