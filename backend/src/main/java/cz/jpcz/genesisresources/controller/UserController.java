package cz.jpcz.genesisresources.controller;

import cz.jpcz.genesisresources.dto.UserDTO;
import cz.jpcz.genesisresources.dto.UserDetailDTO;
import cz.jpcz.genesisresources.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<? extends UserDTO> getUser(@PathVariable Long id,
                                           @RequestParam(value = "detail", defaultValue = "false") boolean detail) {
        log.info("Received request for fetching user with id {} with detail {}", id, detail);
        if (detail) return ResponseEntity.ok(userService.getDetailDTOUser(id));
        return ResponseEntity.ok(userService.getBasicDTOUser(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<? extends UserDTO>> getAllUsers(@RequestParam(value = "detail", defaultValue = "false") boolean detail) {
        log.info("Received request for fetching all users with detail {}", detail);
        return ResponseEntity.ok(userService.getAllDTOUsers(detail));
    }

    @PostMapping("/users")
    public ResponseEntity<? extends UserDTO> createUser(@RequestBody UserDetailDTO userDTO) {
        log.info("Received request for creating user with personId {}", userDTO.getPersonId());
        return ResponseEntity.ok(userService.createDTOUser(userDTO));
    }

    @PutMapping("/users")
    public ResponseEntity<? extends UserDTO> updateUser(@RequestBody UserDetailDTO userDTO) {
        log.info("Received request for updating user with id {}", userDTO.getId());
        return ResponseEntity.ok(userService.updateDTOUser(userDTO.getId(), userDTO));
    }

    @DeleteMapping("/users/{id}")
    public HttpStatus deleteUser(@PathVariable Long id) {
        log.info("Received request for deleting user with id {}", id);
        userService.deleteDTOUser(id);
        return HttpStatus.OK;
    }
}