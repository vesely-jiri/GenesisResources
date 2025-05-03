package cz.jpcz.service;

import cz.jpcz.dto.UserDTO;
import cz.jpcz.entity.UserEntity;
import cz.jpcz.exceptions.UserNotFoundException;
import cz.jpcz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void getDTOUser_returnsUser_whenUserExists() {
        UserEntity userEntity = new UserEntity("Juraj", "Kovář", "xF9hD2yJ3sWv");
        userEntity.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        UserDTO result = userService.getDTOUser(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Juraj");
        assertThat(result.getLastName()).isEqualTo("Kovář");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getDTOUser_throws_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getDTOUser(99L));

        verify(userRepository).findById(99L);
    }
}
