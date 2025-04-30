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

    @GetMapping("/users/{ID}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id,
                                           @RequestParam(value = "detail", defaultValue = "false") boolean detail) {
        try {
            return ResponseEntity.ok(userService.getDTOUser(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/users/{ID}")
    public HttpStatus deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return HttpStatus.OK;
        } catch (UserNotFoundException e) {
            return HttpStatus.NOT_FOUND;
        }
    }
}
