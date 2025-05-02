package cz.jpcz.controller;

import cz.jpcz.dto.UserDTO;
import cz.jpcz.exceptions.UserNotFoundException;
import cz.jpcz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id,
                                           @RequestParam(value = "detail", defaultValue = "false") boolean detail) {
        try {
            return ResponseEntity.ok(userService.getDTOUser(id,detail));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(value = "detail", defaultValue = "false") boolean detail) {
        return ResponseEntity.ok(userService.getAllDTOUsers(detail));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.createDTOUser(userDTO));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/users")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.updateDTOUser(userDTO.getId(), userDTO));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/users/{id}")
    public HttpStatus deleteUser(@PathVariable Long id) {
        try {
            userService.deleteDTOUser(id);
            return HttpStatus.OK;
        } catch (UserNotFoundException e) {
            return HttpStatus.NOT_FOUND;
        }
    }
}
