package ca.bc.gov.open.jag.config;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.OidcClients;
import io.quarkus.oidc.client.Tokens;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.text.MessageFormat;

@ApplicationScoped
public class SSORequestUUIDHeaderFactory implements ClientHeadersFactory {

    private static final Logger logger = LoggerFactory.getLogger(SSORequestUUIDHeaderFactory.class);

    private final OidcClient client;

    public SSORequestUUIDHeaderFactory(OidcClients clients) {

        this.client = clients.getClient("css-sso");

    }

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> clientOutgoingHeaders) {

        logger.info("Getting SSO API token");

        Tokens tokens = client.getTokens().await().indefinitely();

        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("Authorization", MessageFormat.format("Bearer {0}", tokens.getAccessToken()));
        return result;

    }
}