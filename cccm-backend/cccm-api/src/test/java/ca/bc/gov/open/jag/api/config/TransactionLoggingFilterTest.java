package ca.bc.gov.open.jag.api.config;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.*;

@QuarkusTest
public class TransactionLoggingFilterTest {


    @Test
    @DisplayName("Success: void should execute")
    public void testTransactionLog() {

        ContainerRequestContextStub containerRequestContextStub = new ContainerRequestContextStub();
        containerRequestContextStub.headers.putSingle("X-Transaction-Id", "TEST");
        containerRequestContextStub.headers.putSingle("X-Location-Id", "TEST");

        TransactionLoggingFilter sut = new TransactionLoggingFilter();

        Assertions.assertDoesNotThrow(() ->  sut.filter(containerRequestContextStub));

    }

      class ContainerRequestContextStub implements ContainerRequestContext {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();

        SecurityContextStub securityContext = new SecurityContextStub();

        ContainerRequestContextStub(){}

          @Override
          public Object getProperty(String s) {
              return null;
          }

          @Override
          public Collection<String> getPropertyNames() {
              return null;
          }

          @Override
          public void setProperty(String s, Object o) {

          }

          @Override
          public void removeProperty(String s) {

          }

          @Override
          public UriInfo getUriInfo() {
              return null;
          }

          @Override
          public void setRequestUri(URI uri) {

          }

          @Override
          public void setRequestUri(URI uri, URI uri1) {

          }

          @Override
          public Request getRequest() {
              return null;
          }

          @Override
          public String getMethod() {
              return null;
          }

          @Override
          public void setMethod(String s) {

          }

          @Override public MultivaluedMap<String, String> getHeaders() { return headers; }

          @Override
          public String getHeaderString(String s) {
              return null;
          }

          @Override
          public Date getDate() {
              return null;
          }

          @Override
          public Locale getLanguage() {
              return null;
          }

          @Override
          public int getLength() {
              return 0;
          }

          @Override
          public MediaType getMediaType() {
              return null;
          }

          @Override
          public List<MediaType> getAcceptableMediaTypes() {
              return null;
          }

          @Override
          public List<Locale> getAcceptableLanguages() {
              return null;
          }

          @Override
          public Map<String, Cookie> getCookies() {
              return null;
          }

          @Override
          public boolean hasEntity() {
              return false;
          }

          @Override
          public InputStream getEntityStream() {
              return null;
          }

          @Override
          public void setEntityStream(InputStream inputStream) {

          }

          @Override public SecurityContext getSecurityContext() { return securityContext; }

          @Override
          public void setSecurityContext(SecurityContext securityContext) {

          }

          @Override
          public void abortWith(Response response) {

          }

      }

    class SecurityContextStub implements SecurityContext {

        SecurityContextStub(){}

        UserPrincipal userPrincipal = new UserPrincipal() {
            @Override
            public String getName() {
                return "TEST";
            }
        };
        @Override
        public Principal getUserPrincipal() {
            return userPrincipal;
        }

        @Override
        public boolean isUserInRole(String s) {
            return false;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public String getAuthenticationScheme() {
            return null;
        }
    }
}
