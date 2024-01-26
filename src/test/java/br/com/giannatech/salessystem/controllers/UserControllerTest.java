package br.com.giannatech.salessystem.controllers;

import br.com.giannatech.salessystem.entities.User;
import br.com.giannatech.salessystem.entities.dtos.UserDTO;
import br.com.giannatech.salessystem.enums.UserType;
import br.com.giannatech.salessystem.exceptions.InvalidParamsException;
import br.com.giannatech.salessystem.exceptions.UserNotFoundException;
import br.com.giannatech.salessystem.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  static UserDTO userDTO;
  static User userParams;
  static User user0;

  @BeforeEach
  void setup() {
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
  @DisplayName("create -> Given a userDTO object should create a User with the given params")
  void testGivenUserDTOObject_WhenCreateUser_thenReturnCreatedUser() throws Exception {
    given(userService.create(ArgumentMatchers.isA(UserDTO.class))).willReturn(user0);

    // When / Act
    ResultActions response = mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userDTO)));

    response.andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.fullName", is(userDTO.getFullName())))
        .andExpect(jsonPath("$.email", is(userDTO.getEmail())))
        .andExpect(jsonPath("$.cpfCnpj", is(userDTO.getCpfCnpj())));
  }

  @Test
  @DisplayName("create -> Should throw an exception when the given email already exists in the database")
  void testGivenUserDTOObject_WhenCreateUser_thenThrowAnException() throws Exception {
    given(userService.create(ArgumentMatchers.isA(UserDTO.class))).willThrow(new InvalidParamsException("Email already exists"));

    // When / Act
    ResultActions response = mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userDTO)));

    response.andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.timestamp", notNullValue()))
        .andExpect(jsonPath("$.message", is("Email already exists")))
        .andExpect(jsonPath("$.details", is("uri=/users")));
  }

  @Test
  @DisplayName("create -> Should throw an exception when the given cpf/cpnj already exists in the database")
  void testGivenUserDTOObject_WhenCreateUserAndCpfCnpjAlreadyExists_thenThrowAnException() throws Exception {
    given(userService.create(ArgumentMatchers.isA(UserDTO.class)))
        .willThrow(new InvalidParamsException("CPF/CNPJ already exists"));

    // When / Act
    ResultActions response = mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userDTO)));

    response.andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.timestamp", notNullValue()))
        .andExpect(jsonPath("$.message", is("CPF/CNPJ already exists")))
        .andExpect(jsonPath("$.details", is("uri=/users")));
  }

  @Test
  @DisplayName("findAll -> Should return all the users in the database")
  void testFindAll_WhenUsersExistsInTheDatabase_ShouldReturnThemAll() throws Exception {
    User user1 = new User(2L, "Ada Lovelace", "ada@email.com",
        "12312312322", UserType.PAYER, "123456789");

    given(userService.findAll()).willReturn(List.of(user0, user1));

    // When / Act
    ResultActions response = mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON));

    response.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(2)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].fullName", is(user0.getFullName())))
        .andExpect(jsonPath("$[0].email", is(user0.getEmail())))
        .andExpect(jsonPath("$[0].cpfCnpj", is(user0.getCpfCnpj())))
        .andExpect(jsonPath("$[0].type", is(user0.getType().toString())))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].fullName", is(user1.getFullName())))
        .andExpect(jsonPath("$[1].email", is(user1.getEmail())))
        .andExpect(jsonPath("$[1].cpfCnpj", is(user1.getCpfCnpj())))
        .andExpect(jsonPath("$[1].type", is(user1.getType().toString())));
  }

  @Test
  @DisplayName("delete -> When the user with the given id exists, should delete it")
  void testDelete_WhenTheUserWithTheGivenIdExists_ShouldDeleteIt() throws Exception {

    new User(2L, "Ada Lovelace", "ada@email.com",
        "12312312322", UserType.PAYER, "123456789");

    willDoNothing().given(userService).deleteById(2L);

    // When / Act
    ResultActions response = mockMvc.perform(delete("/users/{id}", "2")
        .contentType(MediaType.APPLICATION_JSON));

    response.andDo(print()).andExpect(status().isNoContent());

  }

  @Test
  @DisplayName("delete -> When the user doesn't exists with the given id exists, should throw an exception")
  void testDelete_WhenTheUserDoesntExistsWithTheGivenIdExists_ShouldThrowAnException() throws Exception {
    willThrow(new UserNotFoundException("User not found")).given(userService).deleteById(2L);

    // When / Act
    ResultActions response = mockMvc.perform(delete("/users/{id}", "2")
        .contentType(MediaType.APPLICATION_JSON));

    response.andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.timestamp", notNullValue()))
        .andExpect(jsonPath("$.message", is("User not found")))
        .andExpect(jsonPath("$.details", is("uri=/users/2")));

  }

  @Test
  @DisplayName("findById -> When the user doesn't exists with the given id exists, should throw an exception")
  void testFindByID_WhenTheUserDoesntExistsWithTheGivenIdExists_ShouldThrowAnException() throws Exception {
    given(userService.findById(2L)).willThrow(new UserNotFoundException("User not found"));

    // When / Act
    ResultActions response = mockMvc.perform(get("/users/{id}", "2")
        .contentType(MediaType.APPLICATION_JSON));

    response.andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.timestamp", notNullValue()))
        .andExpect(jsonPath("$.message", is("User not found")))
        .andExpect(jsonPath("$.details", is("uri=/users/2")));

  }

  @Test
  @DisplayName("findById -> When the user exists with the given id exists, should return it")
  void testFindByID_WhenTheUserDoesntExistsWithTheGivenIdExists_ShouldReturnIt() throws Exception {
    given(userService.findById(any())).willReturn(user0);

    // When / Act
    ResultActions response = mockMvc.perform(get("/users/{id}", "2")
        .contentType(MediaType.APPLICATION_JSON));

    response.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.fullName", is(user0.getFullName())))
        .andExpect(jsonPath("$.email", is(user0.getEmail())))
        .andExpect(jsonPath("$.cpfCnpj", is(user0.getCpfCnpj())))
        .andExpect(jsonPath("$.type", is(user0.getType().toString())));
  }
}