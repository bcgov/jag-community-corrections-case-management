package ca.bc.gov.open.jag.properties;

import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "cccm.ldap")
public class LdapProperties {

    private String username;
    private String password;
    private String server;
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
