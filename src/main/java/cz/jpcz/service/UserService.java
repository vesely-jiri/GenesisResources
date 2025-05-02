package cz.jpcz.service;

import cz.jpcz.dto.UserDTO;
import cz.jpcz.entity.UserEntity;
import cz.jpcz.exceptions.PersonAlreadyExistsException;
import cz.jpcz.exceptions.UserNotFoundException;
import cz.jpcz.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public UserDTO getDTOUser(Long id) {
        return getDTOUser(id, false);
    }

    public UserDTO getDTOUser(Long id, boolean detail) {
        logger.debug("Attempting to fetch user with id {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Unable to fetch: User with id {} not found", id);
                    return new UserNotFoundException("User with id " + id + " not found");
                });

        logger.info("Fetched user with id {}", id);
        if (!detail) return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName());
        return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getPersonId(), userEntity.getUuid());
    }

    public List<UserDTO> getAllDTOUsers(boolean detail) {
        return userRepository.findAll().stream().map(user -> getDTOUser(Long.valueOf(user.getId()), detail)).toList();
    }

    public UserDTO createDTOUser(UserDTO userDTO) {
        logger.debug("Attempting to create user with personId {}", userDTO.getPersonId());
        if (userRepository.existsByPersonId(userDTO.getPersonId())) {
            logger.warn("User with personId {} already exists", userDTO.getPersonId());
            throw new PersonAlreadyExistsException(userDTO.getPersonId());
        }
        UserEntity userEntity = new UserEntity(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPersonId());
        userRepository.save(userEntity);
        logger.info("Created user with ID {}", userEntity.getId());
        return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName());
    }

    public UserDTO updateDTOUser(Long id, UserDTO userDTO) {
        logger.debug("Attempting to update user with ID {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Unable to update! User with id {} not found", id);
                    return new UserNotFoundException("User with id " + id + " not found");
                        });
        logger.info("Updating user with ID {}", id);
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userRepository.save(userEntity);
        logger.debug("Updated user with ID {}", id);
        return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName());
    }

    public void deleteDTOUser(Long id) {
        logger.debug("Attempting to delete user with ID {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Unable to delete: User with id {} not found", id);
                    return new UserNotFoundException("User with id " + id + " not found");
                });
        userRepository.delete(userEntity);
        logger.info("User with ID {} has been successfully deleted", id);
    }
}