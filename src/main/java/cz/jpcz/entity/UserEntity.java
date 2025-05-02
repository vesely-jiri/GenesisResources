package cz.jpcz.entity;

import cz.jpcz.exceptions.UserNotFoundException;
import cz.jpcz.util.UserVerify;
import jakarta.persistence.*;

import java.io.File;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    private String personId;
    @Column(nullable = false, unique = true)
    private String uuid;

    public UserEntity() {}

    public UserEntity(String firstName, String lastName, String personId, String uuid) {
        if (!UserVerify.verify(personId)) {
            throw new UserNotFoundException("Invalid personId");
        }
        if (uuid == null) {
            uuid = java.util.UUID.randomUUID().toString();
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.personId = personId;
        this.uuid = uuid;
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
}
