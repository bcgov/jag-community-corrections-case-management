package ca.bc.gov.open.jag.api.model.service;

public class ClientSearch {

    private String lastName;
    private Boolean soundex;
    private String givenName;
    private  Integer birthYear;
    private  Integer age;
    private Integer range;
    private  String address;
    private  String location;
    private  String gender;
    private  String identifier;
    private String identifierType;
    private String officer;

    public ClientSearch(String lastName, Boolean soundex, String givenName, Integer birthYear, Integer age, Integer range, String address, String location, String gender, String identifier, String identifierType, String officer) {
        this.lastName = lastName;
        this.soundex = soundex;
        this.givenName = givenName;
        this.birthYear = birthYear;
        this.age = age;
        this.range = range;
        this.address = address;
        this.location = location;
        this.gender = gender;
        this.identifier = identifier;
        this.identifierType = identifierType;
        this.officer = officer;
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

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }

    public String getGender() {
        return gender;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getOfficer() { return this.officer; }

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
