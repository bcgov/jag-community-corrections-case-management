package ca.bc.gov.open.jag.api.config;

import io.vertx.core.http.HttpServerRequest;
import org.slf4j.MDC;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class TransactionLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext context) {

        if (context.getHeaders().containsKey("X-Transaction-Id")) {
            MDC.put("transaction-id", context.getHeaders().get("X-Transaction-Id").get(0));
        }

        if (context.getHeaders().containsKey("X-Location-Id")) {
            MDC.put("location-id", context.getHeaders().get("X-Location-Id").get(0));
        }

        MDC.put("user", getNameFromContext(context));
        MDC.put("application", "business-api");

    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MDC.clear();
    }

    private String getNameFromContext(ContainerRequestContext context) {
        if (context.getSecurityContext() == null || context.getSecurityContext().getUserPrincipal() == null) return null;
        return context.getSecurityContext().getUserPrincipal().getName();
    }

}
