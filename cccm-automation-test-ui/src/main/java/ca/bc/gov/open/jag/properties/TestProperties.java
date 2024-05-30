package ca.bc.gov.open.jag.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class TestProperties {

    private String usernameApp;
    private String passwordApp;
    private String baseUrl;
    private String dbConnection;

    public String getUsernameApp() {
        return usernameApp;
    }

    public void setUsernameApp(String usernameApp) {
        this.usernameApp = usernameApp;
    }

    public String getPasswordApp() {
        return passwordApp;
    }

    public void setPasswordApp(String passwordApp) {
        this.passwordApp = passwordApp;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(String dbConnection) {
        this.dbConnection = dbConnection;
    }

}
