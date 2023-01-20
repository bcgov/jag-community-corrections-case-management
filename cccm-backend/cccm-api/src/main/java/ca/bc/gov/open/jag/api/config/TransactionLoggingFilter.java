package ca.bc.gov.open.jag.api.config;

import io.vertx.core.http.HttpServerRequest;
import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class TransactionLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Context
    HttpServerRequest request;


    @Override
    public void filter(ContainerRequestContext context) {

        MDC.put("user", getNameFromContext(context));

    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MDC.remove("user");
    }

    private String getNameFromContext(ContainerRequestContext context) {
        if (context.getSecurityContext() == null || context.getSecurityContext().getUserPrincipal() == null) return null;
        return context.getSecurityContext().getUserPrincipal().getName();
    }



}