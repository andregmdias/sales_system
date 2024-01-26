package br.com.giannatech.salessystem.controllers;

import br.com.giannatech.salessystem.entities.User;
import br.com.giannatech.salessystem.entities.dtos.UserDTO;
import br.com.giannatech.salessystem.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<User> create(@RequestBody UserDTO userDTO) {
    var user = userService.create(userDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @GetMapping
  public ResponseEntity<List<User>> findAll() {
    var users = userService.findAll();
    return ResponseEntity.ok(users);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    var user = userService.findById(id);
    return ResponseEntity.ok(user);
  }
}
