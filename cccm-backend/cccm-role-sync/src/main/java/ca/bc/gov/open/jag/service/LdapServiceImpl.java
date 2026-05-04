package ca.bc.gov.open.jag.service;

import ca.bc.gov.open.jag.factory.CustomLdapSslSocketFactory;
import ca.bc.gov.open.jag.model.IdirUser;
import ca.bc.gov.open.jag.properties.LdapProperties;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.text.MessageFormat;
import java.util.Properties;

import static ca.bc.gov.open.jag.Keys.*;

@ApplicationScoped

public class LdapServiceImpl implements LdapService {

    private static final Logger logger = LoggerFactory.getLogger(LdapServiceImpl.class);

    private final LdapProperties ldapProperties;

    public LdapServiceImpl(LdapProperties ldapProperties) {
        this.ldapProperties = ldapProperties;
    }

    @Override
    public IdirUser getUserByUsername(String username) {

        Properties env = new Properties();
        env.put("com.sun.jndi.ldap.read.timeout", "5000");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, LDAP_SECURITY_AUTHENTICATION);
        env.put(Context.SECURITY_PRINCIPAL, MessageFormat.format("{0}@{1}", ldapProperties.username(), ldapProperties.organization()));
        env.put(Context.SECURITY_CREDENTIALS, ldapProperties.password());
        env.put(Context.PROVIDER_URL, ldapProperties.server());

        //SSL Changes
        if (ldapProperties.sslenable()) {
            env.put(Context.SECURITY_PROTOCOL, LDAP_PROTOCOL);
            env.put("java.naming.ldap.factory.socket", CustomLdapSslSocketFactory.class.getName()); //See: https://docs.oracle.com/javase/jndi/tutorial/ldap/security/ssl.html

            System.setProperty("custom.ldap.truststore.type", ldapProperties.truststoretype());
            System.setProperty("custom.ldap.truststore.loc", ldapProperties.truststoreloc());
            System.setProperty("custom.ldap.truststore.password", ldapProperties.truststorepassword());
            System.setProperty("custom.ldap.ssl.protocol", ldapProperties.sslprotocol());
        }

        IdirUser idirUser = null;
        DirContext searchContext = null;
        try {

            searchContext = new InitialDirContext(env);
            String[] attrIDs = { IDIR_GUID, IDIR_SN, IDIR_GIVEN_NAME, IDIR_MAIL };
            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(attrIDs);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> searchResults
                    = searchContext.search(ldapProperties.distinguishedname(), MessageFormat.format(IDIR_FILTER, username), searchControls);

            if (searchResults.hasMore()) {

                SearchResult result = searchResults.next();
                Attributes attrs = result.getAttributes();

                idirUser = new IdirUser();
                idirUser.setGuid(attrs.get(IDIR_GUID).get(0).toString());
                idirUser.setEmail(attrs.get(IDIR_MAIL).get(0).toString());
                idirUser.setFirstName(attrs.get(IDIR_GIVEN_NAME).get(0).toString());
                idirUser.setLastName(attrs.get(IDIR_SN).get(0).toString());

            }

        } catch (Exception e) {
            logger.error("Error in idir lookup: ", e);
        } finally {
            if (searchContext != null) {
                try {
                    searchContext.close();
                } catch (NamingException e) {
                    logger.error("Error closing connection: ", e);
                }
            }
        }

        return idirUser;

    }

}
