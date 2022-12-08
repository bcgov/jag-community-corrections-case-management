package ca.bc.gov.open.jag.config;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.Tokens;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.text.MessageFormat;

@ApplicationScoped
public class RequestUUIDHeaderFactory implements ClientHeadersFactory {

    private final OidcClient client;

    public  RequestUUIDHeaderFactory(OidcClient client) {

        this.client = client;

    }

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> clientOutgoingHeaders) {

        Tokens tokens = client.getTokens().await().indefinitely();

        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("Authorization", MessageFormat.format("Bearer {0}", tokens.getAccessToken()));
        return result;

    }
}