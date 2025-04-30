package cz.jpcz.service;

import cz.jpcz.dto.UserDTO;
import cz.jpcz.entity.UserEntity;
import cz.jpcz.exceptions.UserNotFoundException;
import cz.jpcz.model.UserModel;
import cz.jpcz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel getUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        return new UserModel(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getPersonId(), userEntity.getUuid());
    }

    public UserDTO getDTOUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getPersonId(), userEntity.getUuid());
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userEntity -> new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                        userEntity.getPersonId(), userEntity.getUuid())).toList();
    }

    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        userRepository.delete(userEntity);
    }
}
