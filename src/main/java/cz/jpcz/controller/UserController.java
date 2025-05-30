package cz.jpcz.controller;

import cz.jpcz.dto.UserDTO;
import cz.jpcz.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id,
                                           @RequestParam(value = "detail", defaultValue = "false") boolean detail) {
        logger.info("Received request for fetching user with id {} with detail {}", id, detail);
            return ResponseEntity.ok(userService.getDTOUser(id,detail));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(value = "detail", defaultValue = "false") boolean detail) {
        logger.info("Received request for fetching all users with detail {}", detail);
        return ResponseEntity.ok(userService.getAllDTOUsers(detail));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        logger.info("Received request for creating user with personId {}", userDTO.getPersonId());
        return ResponseEntity.ok(userService.createDTOUser(userDTO));
    }

    @PutMapping("/users")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        logger.info("Received request for updating user with id {}", userDTO.getId());
        return ResponseEntity.ok(userService.updateDTOUser(userDTO.getId(), userDTO));
    }

    @DeleteMapping("/users/{id}")
    public HttpStatus deleteUser(@PathVariable Long id) {
        logger.info("Received request for deleting user with id {}", id);
        userService.deleteDTOUser(id);
        return HttpStatus.OK;
    }
}