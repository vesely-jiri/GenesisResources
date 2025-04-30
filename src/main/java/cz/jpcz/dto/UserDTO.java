package cz.jpcz.dto;

public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String personId;
    private String uuid;

    public UserDTO(long id, String firstName, String lastName, String personId, String uuid) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personId = personId;
        this.uuid = uuid;
    }

    public long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPersonId() {
        return personId;
    }
    public String getUuid() {
        return uuid;
    }
}
