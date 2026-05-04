package ca.bc.gov.open.jag.properties;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "cccm.ldap")
public interface LdapProperties {

    String username();
    String password();
    String server();
    String organization();
    String distinguishedname();

    String truststoretype();
    String truststoreloc();
    String truststorepassword();
    String sslprotocol();

    boolean sslenable();

}
