package ca.bc.gov.open.jag.service;

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

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
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

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);

        sut = new RoleSyncServiceImpl(cccmApiServiceMock, keycloakMock, "REALM", true);

    }

    @Test
    public void testProcessAll() {

        Mockito.when(cccmApiServiceMock.getUsers(any())).thenReturn(new ArrayList<>());
        Mockito.when(usersResourceMock.search(any())).thenReturn(new ArrayList<>());
        Mockito.when(usersResourceMock.list()).thenReturn(new ArrayList<>());
        Mockito.when(usersResourceMock.get(any())).thenReturn(null);
        Mockito.when(realmMock.users()).thenReturn(usersResourceMock);
        Mockito.when(realmMock.groups()).thenReturn(groupsResourceMock);
        Mockito.when(groupsResourceMock.groups()).thenReturn(new ArrayList<>());
        Mockito.when(keycloakMock.realm(any())).thenReturn(realmMock);


        Assertions.assertDoesNotThrow(() -> sut.syncRoles());


    }

}
