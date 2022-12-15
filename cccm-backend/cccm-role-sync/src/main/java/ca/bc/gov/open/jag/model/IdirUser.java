package ca.bc.gov.open.jag.model;

import java.util.HashMap;
import java.util.List;

public class IdirUser {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    HashMap<String, List<String>> attributes;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, List<String>> attributes) {
        this.attributes = attributes;
    }
}
