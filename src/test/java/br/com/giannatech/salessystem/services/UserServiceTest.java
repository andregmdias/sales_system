package br.com.giannatech.salessystem.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import br.com.giannatech.salessystem.entities.User;
import br.com.giannatech.salessystem.entities.dtos.UserDTO;
import br.com.giannatech.salessystem.enums.UserType;
import br.com.giannatech.salessystem.exceptions.InvalidParamsException;
import br.com.giannatech.salessystem.exceptions.UserNotFoundException;
import br.com.giannatech.salessystem.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @Mock
  ModelMapper modelMapper;

  @InjectMocks
  UserService userService;

  static UserDTO userDTO;
  static User userParams;
  static User user0;

  @BeforeAll
  static void setup() {
    System.out.println("Starting tests for UserService");

    userDTO = new UserDTO("Foo Bar Baz", "foobar@email.com",
        "12312312311",
        UserType.PAYEE.toString(), "12345678");

    userParams = new User("Foo Bar Baz", "foobar@email.com",
        "12312312311", UserType.PAYEE, "12345678");

    user0 = new User(1L, "Foo Bar Baz", "foobar@email.com",
        "12312312311", UserType.PAYEE, "12345678");
  }

  @Test
  @DisplayName("create -> Should create a new user")
  void create_should_create_a_new_user() {

    given(userRepository.findByEmail(userDTO.getEmail())).willReturn(Optional.empty());
    given(userRepository.findByCpfCnpj(userDTO.getCpfCnpj())).willReturn(Optional.empty());
    given(modelMapper.map(userDTO, User.class)).willReturn(userParams);
    given(userRepository.save(Mockito.any(User.class))).willReturn(user0);

    var result = userService.create(userDTO);

    verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
    verify(userRepository, times(1)).findByCpfCnpj(userDTO.getCpfCnpj());
    verify(userRepository, times(1)).save(Mockito.any(User.class));

    assertInstanceOf(User.class, user0);
    assertNotNull(result.getId());
    assertEquals(userDTO.getFullName(), result.getFullName());
    assertEquals(userDTO.getEmail(), result.getEmail());
    assertEquals(userDTO.getCpfCnpj(), result.getCpfCnpj());
    assertEquals(userDTO.getType(), result.getType().toString());
  }

  @Test
  @DisplayName("create -> Should throw an exception when email already exists")
  void create_should_throw_an_exception_when_email_already_exists() {
    UserDTO userDTO = new UserDTO("Foo Bar Baz", "foobar@email.com", "12312312311",
        UserType.PAYEE.toString(), "12345678");

    User user0 = new User(1L, "Foo Bar Baz", "foobar@email.com",
        "12312312311", UserType.PAYEE, "12345678");

    given(userRepository.findByEmail(userDTO.getEmail())).willReturn(Optional.of(user0));

    Exception result = assertThrows(InvalidParamsException.class, () -> userService.create(userDTO));

    verify(userRepository, only()).findByEmail(userDTO.getEmail());
    verify(userRepository, never()).findByCpfCnpj(userDTO.getCpfCnpj());
    verify(userRepository, never()).save(any(User.class));

    assertEquals("Email already exists", result.getMessage());
  }

  @Test
  @DisplayName("create -> Should throw an exception when CPF/CNPJ already exists")
  void create_should_throw_an_exception_when_cpf_cnpj_already_exists() {
    UserDTO userDTO = new UserDTO("Foo Bar Baz", "foobar@email.com", "12312312311",
        UserType.PAYEE.toString(), "12345678");

    User user0 = new User(1L, "Foo Bar Baz", "foobar@email.com",
        "12312312311", UserType.PAYEE, "12345678");

    given(userRepository.findByEmail(userDTO.getEmail())).willReturn(Optional.empty());
    given(userRepository.findByCpfCnpj(userDTO.getCpfCnpj())).willReturn(Optional.of(user0));

    Exception result = assertThrows(InvalidParamsException.class, () -> userService.create(userDTO));

    verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
    verify(userRepository, times(1)).findByCpfCnpj(userDTO.getCpfCnpj());
    verify(userRepository, never()).save(any(User.class));

    assertEquals("CPF/CNPJ already exists", result.getMessage());
  }

  @Test
  @DisplayName("findAll -> Should return all users in the database")
  void findAll_should_return_all_users_in_the_database() {
    var user1 = new User(1L, "Foo Bar Baz", "foobar@email.com",
        "12312312311", UserType.PAYEE, "12345678");

    var allUsers = List.of(user0, user1);

    given(userRepository.findAll()).willReturn(allUsers);

    List<User> result = userService.findAll();

    verify(userRepository, times(1)).findAll();

    assertEquals(2, result.size());
    assertEquals(user0.getId(), result.get(0).getId());
    assertEquals(user1.getId(), result.get(1).getId());
  }

  @Test
  @DisplayName("deleteById -> Should delete a user by id")
  void deleteById_should_delete_a_user_by_id() {
    given(userRepository.findById(user0.getId())).willReturn(Optional.of(user0));

    userService.deleteById(user0.getId());

    verify(userRepository, times(1)).findById(user0.getId());
    verify(userRepository, times(1)).delete(user0);
  }

  @Test
  @DisplayName("deleteById -> Should throw an exception when user not found")
  void deleteById_should_throw_an_exception_when_user_not_found() {
    given(userRepository.findById(user0.getId())).willReturn(Optional.empty());

    Exception result = assertThrows(UserNotFoundException.class, () -> userService.deleteById(user0.getId()));

    verify(userRepository, times(1)).findById(user0.getId());
    verify(userRepository, never()).delete(user0);

    assertEquals("User not found", result.getMessage());
  }

  @Test
  @DisplayName("findById -> Should find a user by id")
  void findById_should_find_by_user_id() {
    given(userRepository.findById(user0.getId())).willReturn(Optional.of(user0));

    var result = userService.findById(user0.getId());

    verify(userRepository, times(1)).findById(user0.getId());

    assertInstanceOf(User.class, user0);
    assertNotNull(result.getId());
    assertEquals(user0.getFullName(), result.getFullName());
    assertEquals(user0.getEmail(), result.getEmail());
  }

  @Test
  @DisplayName("findById -> Should throw an exception when user not found")
  void findById_should_throw_an_exception_when_user_not_found() {
    given(userRepository.findById(user0.getId())).willReturn(Optional.empty());

    Exception result = assertThrows(UserNotFoundException.class, () -> userService.deleteById(user0.getId()));

    verify(userRepository, times(1)).findById(user0.getId());

    assertEquals("User not found", result.getMessage());
  }
}
