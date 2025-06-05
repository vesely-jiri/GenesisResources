package cz.jpcz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String personId;
    @Getter
    private String uuid;

    public UserDTO() {}

    public UserDTO(long id, String firstName, String lastName, String personId, String uuid) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personId = personId;
        this.uuid = uuid;
    }

    public UserDTO(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserDTO(String firstName, String lastName, String personId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personId = personId;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
