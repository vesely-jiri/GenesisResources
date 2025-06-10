package cz.jpcz.genesisresources.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailDTO extends UserDTO {
    private String personId;
    private String uuid;
}
