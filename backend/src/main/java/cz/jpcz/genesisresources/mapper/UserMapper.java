package cz.jpcz.genesisresources.mapper;

import cz.jpcz.genesisresources.dto.UserDTO;
import cz.jpcz.genesisresources.dto.UserDetailDTO;
import cz.jpcz.genesisresources.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(UserEntity userEntity);
    UserDetailDTO toDetailDTO(UserEntity userEntity);
    UserEntity toEntity(UserDTO userDTO);

}
