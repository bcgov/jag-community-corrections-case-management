package ca.bc.gov.open.jag.api.model.service;

public class ClientSearch {

    private String name;
    private Boolean soundex;
    private  Integer birthYear;
    private  Integer age;
    private  String address;
    private  String location;
    private  String gender;
    private  String clientNum;

    public ClientSearch(String name, Boolean soundex, Integer birthYear, Integer age, String address, String location, String gender, String clientNum) {
        this.name = name;
        this.soundex = soundex;
        this.birthYear = birthYear;
        this.age = age;
        this.address = address;
        this.location = location;
        this.gender = gender;
        this.clientNum = clientNum;
    }

    public String getName() {
        return name;
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

    public String getClientNum() {
        return clientNum;
    }
}
