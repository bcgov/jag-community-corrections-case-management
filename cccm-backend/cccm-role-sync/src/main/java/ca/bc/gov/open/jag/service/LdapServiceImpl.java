package ca.bc.gov.open.jag.service;

import ca.bc.gov.open.jag.model.IdirUser;
import ca.bc.gov.open.jag.properties.LdapProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;
import java.util.Properties;

@ApplicationScoped
public class LdapServiceImpl implements LdapService {

    private static final Logger logger = LoggerFactory.getLogger(LdapServiceImpl.class);

    @Inject
    LdapProperties ldapProperties;

    @Override
    public IdirUser getUserByUsername(String username) {

        Properties env = new Properties();
        env.put("com.sun.jndi.ldap.read.timeout", "5000");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn=" + ldapProperties.getUsername() + "," + ldapProperties.getOrganization());
        env.put(Context.SECURITY_CREDENTIALS, ldapProperties.getPassword());
        env.put(Context.PROVIDER_URL, ldapProperties.getServer());

        try {

            DirContext searchContext = new InitialDirContext(env);



        } catch (Exception e) {
            logger.error("Error in idir lookup: ", e);
        }


        return null;
    }

}
