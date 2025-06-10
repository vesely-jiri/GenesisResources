package cz.jpcz.genesisresources.service;

import cz.jpcz.genesisresources.dto.UserDTO;
import cz.jpcz.genesisresources.dto.UserDetailDTO;
import cz.jpcz.genesisresources.entity.UserEntity;
import cz.jpcz.genesisresources.exceptions.PersonAlreadyExistsException;
import cz.jpcz.genesisresources.exceptions.PersonNotFoundException;
import cz.jpcz.genesisresources.exceptions.UserNotFoundException;
import cz.jpcz.genesisresources.mapper.UserMapper;
import cz.jpcz.genesisresources.repository.UserRepository;
import cz.jpcz.genesisresources.util.UserVerify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserDTO getBasicDTOUser(Long id) {
        return userRepository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public UserDetailDTO getDetailDTOUser(Long id) {
        return userRepository.findById(id)
                .map(mapper::toDetailDTO)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public List<UserDTO> getAllDTOUsers(boolean detail) {
        return userRepository.findAll().stream()
                .map(user -> detail ? mapper.toDetailDTO(user) : mapper.toDTO(user))
                .toList();
    }

    public UserDTO createDTOUser(UserDetailDTO userDTO) {
        log.debug("Attempting to create user with personId {}", userDTO.getPersonId());
        if (userRepository.existsByPersonId(userDTO.getPersonId())) {
            log.warn("User with personId {} already exists", userDTO.getPersonId());
            throw new PersonAlreadyExistsException(userDTO.getPersonId());
        }
        UserVerify.validatePerson(userDTO.getPersonId());
        UserEntity userEntity = new UserEntity(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPersonId());
        UserEntity savedEntity = userRepository.save(userEntity);
        log.info("Created user with ID {}", savedEntity.getId());
        return mapper.toDTO(savedEntity);
    }

    public UserDTO updateDTOUser(Long id, UserDetailDTO userDTO) {
        log.debug("Attempting to update user with ID {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Unable to update! User with id {} not found", id);
                    return new UserNotFoundException("User with id " + id + " not found");
                        });
        if (!userEntity.getPersonId().equals(userDTO.getPersonId())) {
            log.warn("Unable to update! PersonId {} does not match", userDTO.getPersonId() + " not found");
            throw new PersonNotFoundException(userDTO.getPersonId());
        }
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userRepository.save(userEntity);
        log.debug("Updated user with ID {}", id);
        return mapper.toDTO(userEntity);
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