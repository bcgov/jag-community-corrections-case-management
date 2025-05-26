package ca.bc.gov.open.jag.config;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.Tokens;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.UniAwait;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.MultivaluedMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestUUIDHeaderFactoryTest {


    RequestUUIDHeaderFactory sut;

    @Mock
    OidcClient clientMock;

    @Mock
    Uni<Tokens> tokensUniMock;

    @Mock
    UniAwait<Tokens> tokensUniAwaitMock;


    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
        Tokens tokens = new Tokens("test", 123L, null, "test", 123L, new JsonObject());

        Mockito.when(tokensUniAwaitMock.indefinitely()).thenReturn(tokens);
        Mockito.when(tokensUniMock.await()).thenReturn(tokensUniAwaitMock);
        Mockito.when(clientMock.getTokens()).thenReturn(tokensUniMock);

        sut = new RequestUUIDHeaderFactory(clientMock);

    }

    @Test
    public void testHeaderAdded() {

        MultivaluedMap<String, String> result = sut.update(null, null);

        Assertions.assertEquals("Bearer test", result.getFirst("Authorization"));

    }
}
