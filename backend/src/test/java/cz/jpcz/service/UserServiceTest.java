package cz.jpcz.service;

import cz.jpcz.dto.UserDTO;
import cz.jpcz.entity.UserEntity;
import cz.jpcz.exceptions.PersonAlreadyExistsException;
import cz.jpcz.exceptions.UserNotFoundException;
import cz.jpcz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//Testing personId: xF9hD2yJ3sWv
public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void getDTOUser_returnsBasicUser_whenExistsAndDetailFalse() {
        UserEntity userEntity = new UserEntity("Juraj", "Kovář", "xF9hD2yJ3sWv");
        userEntity.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        UserDTO dto = userService.getDTOUser(1L);
        assertThat(dto).isNotNull();
        assertThat(dto.getFirstName()).isEqualTo("Juraj");
        assertThat(dto.getLastName()).isEqualTo("Kovář");
        verify(userRepository).findById(1L);
    }

    @Test
    void getDTOUser_returnsDetailedUser_whenDetailTrue() {
        UserEntity userEntity = new UserEntity("Juraj", "Kovář", "xF9hD2yJ3sWv");
        userEntity.setId(1L);
        userEntity.setUuid("abc-123");
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        UserDTO dto = userService.getDTOUser(1L, true);
        assertThat(dto).isNotNull();
        assertThat(dto.getPersonId()).isEqualTo("xF9hD2yJ3sWv");
        assertThat(dto.getUuid()).isEqualTo("abc-123");
    }

    @Test
    void getDTOUser_throws_whenUserNotFound() {
        when(userRepository.findById(42L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getDTOUser(42L));
    }

    @Test
    void getAllDTOUsers_returnsAllConverted() {
        UserEntity user1 = new UserEntity("A", "B", "xF9hD2yJ3sWv");
        user1.setId(1L);
        UserEntity user2 = new UserEntity("C", "D", "xF9hD2yJ3sWv");
        user2.setId(2L);
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        List<UserDTO> result = userService.getAllDTOUsers(false);
        assertThat(result).hasSize(2);
        verify(userRepository).findAll();
    }

    @Test
    void createDTOUser_createsSuccessfully_whenNotExists() {
        UserDTO input = new UserDTO("Juraj", "Kovář", "xF9hD2yJ3sWv");
        when(userRepository.existsByPersonId("xF9hD2yJ3sWv")).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setId(100L);
            return user;
        });
        UserDTO result = userService.createDTOUser(input);
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getFirstName()).isEqualTo("Juraj");
        verify(userRepository).save(any(UserEntity.class));
    }



    @Test
    void createDTOUser_throws_whenPersonAlreadyExists() {
        UserDTO input = new UserDTO("Dup", "User", "xF9hD2yJ3sWv");
        when(userRepository.existsByPersonId("xF9hD2yJ3sWv")).thenReturn(true);
        assertThrows(PersonAlreadyExistsException.class, () -> userService.createDTOUser(input));
    }

    @Test
    void updateDTOUser_updatesFieldsCorrectly() {
        UserDTO update = new UserDTO("Updated", "Name", "xF9hD2yJ3sWv");
        UserEntity existing = new UserEntity("Old", "Name", "xF9hD2yJ3sWv");
        existing.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        UserEntity saved = new UserEntity("Updated", "Name", "xF9hD2yJ3sWv");
        saved.setId(1L);
        when(userRepository.save(existing)).thenReturn(saved);
        UserDTO result = userService.updateDTOUser(1L, update);
        assertThat(result.getFirstName()).isEqualTo("Updated");
        verify(userRepository).save(existing);
    }

    @Test
    void updateDTOUser_throws_whenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        UserDTO update = new UserDTO(null, "X", "Y");
        assertThrows(UserNotFoundException.class, () -> userService.updateDTOUser(999L, update));
    }

    @Test
    void deleteDTOUser_deletes_whenFound() {
        UserEntity existing = new UserEntity("To", "Delete", "xF9hD2yJ3sWv");
        existing.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        userService.deleteDTOUser(1L);
        verify(userRepository).delete(existing);
    }

    @Test
    void deleteDTOUser_throws_whenNotFound() {
        when(userRepository.findById(888L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteDTOUser(888L));
    }
}
