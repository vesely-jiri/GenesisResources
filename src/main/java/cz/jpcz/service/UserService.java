package cz.jpcz.service;

import cz.jpcz.dto.UserDTO;
import cz.jpcz.entity.UserEntity;
import cz.jpcz.exceptions.UserNotFoundException;
import cz.jpcz.model.UserModel;
import cz.jpcz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO getDTOUser(Long id) {
        return getDTOUser(id, false);
    }

    public UserDTO getDTOUser(Long id, boolean detail) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        if (!detail) return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName());
        return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getPersonId(), userEntity.getUuid());
    }

    public List<UserDTO> getAllDTOUsers(boolean detail) {
        return userRepository.findAll().stream().map(user -> getDTOUser(Long.valueOf(user.getId()), detail)).toList();
    }

    public UserDTO updateDTOUser(Long id, UserDTO userDTO) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userRepository.save(userEntity);
        return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName());
    }

    public void deleteDTOUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        userRepository.delete(userEntity);
    }
}