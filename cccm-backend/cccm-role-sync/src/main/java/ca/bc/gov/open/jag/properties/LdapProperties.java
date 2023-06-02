package ca.bc.gov.open.jag.properties;

import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LdapProperties {

    @ConfigProperty(name = "cccm.ldap.username")
    private String username;
    @ConfigProperty(name = "cccm.ldap.password")
    private String password;
    @ConfigProperty(name = "cccm.ldap.server")
    private String server;
    @ConfigProperty(name = "cccm.ldap.organization")
    private String organization;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

}
