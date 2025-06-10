package cz.jpcz.genesisresources.service;

import cz.jpcz.genesisresources.dto.UserDTO;
import cz.jpcz.genesisresources.dto.UserDetailDTO;
import cz.jpcz.genesisresources.entity.UserEntity;
import cz.jpcz.genesisresources.exceptions.PersonAlreadyExistsException;
import cz.jpcz.genesisresources.exceptions.UserNotFoundException;
import cz.jpcz.genesisresources.mapper.UserMapper;
import cz.jpcz.genesisresources.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//Testing personId: xF9hD2yJ3sWv
public class UserServiceTest {

    private UserRepository repository;
    private UserService service;
    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        mapper = mock(UserMapper.class);
        service = new UserService(repository,mapper);
    }

    @Test
    void getDTOUser_returnsBasicUser_whenExistsAndDetailFalse() {
        UserEntity userEntity = new UserEntity("Juraj", "Kovář", "xF9hD2yJ3sWv");
        userEntity.setId(1L);

        UserDTO mappedDTO = new UserDTO();
        mappedDTO.setId(1L);
        mappedDTO.setFirstName("Juraj");
        mappedDTO.setLastName("Kovář");

        when(repository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(mapper.toDTO(userEntity)).thenReturn(mappedDTO);
        UserDTO dto = service.getBasicDTOUser(1L);

        assertThat(dto).isNotNull();
        assertThat(dto.getFirstName()).isEqualTo("Juraj");
        assertThat(dto.getLastName()).isEqualTo("Kovář");
        verify(repository).findById(1L);
    }

    @Test
    void getDTOUser_returnsDetailedUser_whenDetailTrue() {
        UserEntity userEntity = new UserEntity("Juraj", "Kovář", "xF9hD2yJ3sWv");
        userEntity.setId(1L);
        userEntity.setUuid("abc-123");

        UserDetailDTO mappedDTO = new UserDetailDTO();
        mappedDTO.setId(1L);
        mappedDTO.setFirstName("Juraj");
        mappedDTO.setLastName("Kovář");
        mappedDTO.setPersonId("xF9hD2yJ3sWv");
        mappedDTO.setUuid("abc-123");

        when(repository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(mapper.toDetailDTO(userEntity)).thenReturn(mappedDTO);
        UserDetailDTO dto = service.getDetailDTOUser(1L);

        assertThat(dto).isNotNull();
        assertThat(dto.getPersonId()).isEqualTo("xF9hD2yJ3sWv");
        assertThat(dto.getUuid()).isEqualTo("abc-123");
    }

    @Test
    void getDTOUser_throws_whenUserNotFound() {
        when(repository.findById(42L)).thenReturn(Optional.empty());
        when(mapper.toDetailDTO(any(UserEntity.class))).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> service.getBasicDTOUser(42L));
    }

    @Test
    void getAllDTOUsers_returnsAllConverted() {
        UserEntity user1 = new UserEntity("A", "B", "xF9hD2yJ3sWv");
        user1.setId(1L);
        UserEntity user2 = new UserEntity("C", "D", "xF9hD2yJ3sWv");
        user2.setId(2L);

        UserDTO mappedDTO = new UserDTO();
        mappedDTO.setId(1L);
        mappedDTO.setFirstName("A");
        mappedDTO.setLastName("B");

        UserDTO mappedDTO2 = new UserDTO();
        mappedDTO2.setId(2L);
        mappedDTO2.setFirstName("C");
        mappedDTO2.setLastName("D");

        when(repository.findAll()).thenReturn(List.of(user1, user2));
        when(repository.findById(1L)).thenReturn(Optional.of(user1));
        when(repository.findById(2L)).thenReturn(Optional.of(user2));
        when(mapper.toDTO(user1)).thenReturn(mappedDTO);
        when(mapper.toDTO(user2)).thenReturn(mappedDTO2);
        List<UserDTO> result = service.getAllDTOUsers(false);

        assertThat(result).hasSize(2);
        verify(repository).findAll();
    }

    @Test
    void createDTOUser_createsSuccessfully_whenNotExists() {
        UserDetailDTO input = new UserDetailDTO();
        input.setFirstName("Juraj");
        input.setLastName("Kovář");
        input.setPersonId("xF9hD2yJ3sWv");

        when(repository.existsByPersonId("xF9hD2yJ3sWv")).thenReturn(false);

        when(repository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setId(100L);
            return user;
        });

        when(mapper.toDTO(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            return dto;
        });

        UserDTO result = service.createDTOUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getFirstName()).isEqualTo("Juraj");
        verify(repository).save(any(UserEntity.class));
    }


    @Test
    void createDTOUser_throws_whenPersonAlreadyExists() {
        UserDetailDTO input = new UserDetailDTO();
        input.setFirstName("Dup");
        input.setLastName("User");
        input.setPersonId("xF9hD2yJ3sWv");
        when(repository.existsByPersonId("xF9hD2yJ3sWv")).thenReturn(true);

        assertThrows(PersonAlreadyExistsException.class, () -> service.createDTOUser(input));
    }

    @Test
    void updateDTOUser_updatesFieldsCorrectly() {
        UserEntity existing = new UserEntity("Old", "Name", "xF9hD2yJ3sWv");
        existing.setId(1L);

        UserDetailDTO dto = new UserDetailDTO();
        dto.setFirstName("Updated");
        dto.setLastName("Name");
        dto.setPersonId("xF9hD2yJ3sWv");

        UserDTO expectedDTO = new UserDTO();
        expectedDTO.setFirstName("Updated");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(mapper.toDTO(existing)).thenReturn(expectedDTO);
        when(repository.save(existing)).thenReturn(existing);

        UserDTO result = service.updateDTOUser(1L, dto);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Updated");
        verify(repository).save(existing);
    }



    @Test
    void updateDTOUser_throws_whenUserNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        UserDetailDTO update = new UserDetailDTO();
        update.setFirstName(null);
        update.setLastName("X");
        update.setPersonId("Y");

        assertThrows(UserNotFoundException.class, () -> service.updateDTOUser(999L, update));
    }

    @Test
    void deleteDTOUser_deletes_whenFound() {
        UserEntity existing = new UserEntity("To", "Delete", "xF9hD2yJ3sWv");
        existing.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        service.deleteDTOUser(1L);

        verify(repository).delete(existing);
    }

    @Test
    void deleteDTOUser_throws_whenNotFound() {
        when(repository.findById(888L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.deleteDTOUser(888L));
    }
}
