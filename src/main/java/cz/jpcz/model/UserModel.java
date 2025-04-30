package cz.jpcz.model;

import cz.jpcz.dto.UserDTO;

public class UserModel {

    private final Integer id;
    private String firstName;
    private String lastName;
    private String personId;
    private String uuid;

    public UserModel(Integer id, String firstName, String lastName, String personId, String uuid) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personId = personId;
        this.uuid = uuid;
    }

    public UserModel(Integer id, String firstName, String personId, String uuid) {
        this(id, firstName, null, personId, uuid);
    }

    public Integer getId() {
        return id;
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
    public String getPersonId() {
        return personId;
    }
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public UserDTO mapToDTO() {
        return new UserDTO(id, firstName, lastName, personId, uuid);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", personId='" + personId + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}