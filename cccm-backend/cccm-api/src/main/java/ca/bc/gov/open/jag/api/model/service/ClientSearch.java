package ca.bc.gov.open.jag.api.model.service;

public class ClientSearch {

    private String lastName;
    private Boolean soundex;
    private String givenName;
    private  Integer birthYear;
    private  Integer age;
    private Integer range;
    private  Boolean location;
    private  String gender;
    private  String identifier;
    private String identifierType;

    public ClientSearch(String lastName, Boolean soundex, String givenName, Integer birthYear, Integer age, Integer range, Boolean location, String gender, String identifier, String identifierType) {
        this.lastName = lastName;
        this.soundex = soundex;
        this.givenName = givenName;
        this.birthYear = birthYear;
        this.age = age;
        this.range = range;
        this.location = location;
        this.gender = gender;
        this.identifier = identifier;
        this.identifierType = identifierType;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getSoundex() {
        return soundex;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public Integer getAge() {
        return age;
    }

    public Boolean getLocation() {
        return location;
    }

    public String getGender() {
        return gender;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getGivenName() {
        return givenName;
    }

    public Integer getRange() {
        return range;
    }

    public String getIdentifierType() {
        return identifierType;
    }

}
