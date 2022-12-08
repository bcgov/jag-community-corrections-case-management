package ca.bc.gov.open.jag.service;

import io.quarkus.test.Mock;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

//@QuarkusTest
public class RoleSyncServiceTest {

    @Inject
    RoleSyncServiceImpl roleSyncService;

    @InjectMock
    CCCMApiService cccmApiServiceMock;

    @InjectMock
    Keycloak keycloakMock;

    @Mock
    RealmResource realmMock;

    @Mock
    UsersResource usersResourceMock;

    //@Test
    public void testProcessAll() {

        Mockito.when(cccmApiServiceMock.getUsers(any())).thenReturn(new ArrayList<>());
        Mockito.when(usersResourceMock.search(any())).thenReturn(new ArrayList<>());
        Mockito.when(realmMock.users()).thenReturn(usersResourceMock);
        Mockito.when(keycloakMock.realm(any())).thenReturn(realmMock);

        Assertions.assertDoesNotThrow(() -> roleSyncService.syncRoles());


    }

}
