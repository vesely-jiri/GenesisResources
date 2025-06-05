package cz.jpcz.service;

import cz.jpcz.dto.UserDTO;
import cz.jpcz.entity.UserEntity;
import cz.jpcz.exceptions.PersonAlreadyExistsException;
import cz.jpcz.exceptions.PersonNotFoundException;
import cz.jpcz.exceptions.UserNotFoundException;
import cz.jpcz.repository.UserRepository;
import cz.jpcz.util.UserVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getDTOUser(Long id) {
        return getDTOUser(id, false);
    }

    public UserDTO getDTOUser(Long id, boolean detail) {
        log.debug("Attempting to fetch user with id {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Unable to fetch: User with id {} not found", id);
                    return new UserNotFoundException("User with id " + id + " not found");
                });

        log.info("Fetched user with id {}", id);
        if (!detail) return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName());
        return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getPersonId(), userEntity.getUuid());
    }

    public List<UserDTO> getAllDTOUsers(boolean detail) {
        return userRepository.findAll().stream().map(user -> getDTOUser(user.getId(), detail)).toList();
    }

    public UserDTO createDTOUser(UserDTO userDTO) {
        log.debug("Attempting to create user with personId {}", userDTO.getPersonId());
        if (userRepository.existsByPersonId(userDTO.getPersonId())) {
            log.warn("User with personId {} already exists", userDTO.getPersonId());
            throw new PersonAlreadyExistsException(userDTO.getPersonId());
        }
        UserVerify.validatePerson(userDTO.getPersonId());
        UserEntity userEntity = new UserEntity(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPersonId());
        userRepository.save(userEntity);
        log.info("Created user with ID {}", userEntity.getId());
        return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName());
    }

    public UserDTO updateDTOUser(Long id, UserDTO userDTO) {
        log.debug("Attempting to update user with ID {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Unable to update! User with id {} not found", id);
                    return new UserNotFoundException("User with id " + id + " not found");
                        });
        log.info("Updating user with ID {}", id);
        if (!userEntity.getPersonId().equals(userDTO.getPersonId())) {
            log.warn("Unable to update! PersonId {} does not match", userDTO.getPersonId() + " not found");
            throw new PersonNotFoundException(userDTO.getPersonId());
        }
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userRepository.save(userEntity);
        log.debug("Updated user with ID {}", id);
        return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName());
    }

    public void deleteDTOUser(Long id) {
        log.debug("Attempting to delete user with ID {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Unable to delete: User with id {} not found", id);
                    return new UserNotFoundException("User with id " + id + " not found");
                });
        userRepository.delete(userEntity);
        log.info("User with ID {} has been successfully deleted", id);
    }
}