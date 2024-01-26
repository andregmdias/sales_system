package br.com.giannatech.salessystem.repositories;

import br.com.giannatech.salessystem.entities.User;
import br.com.giannatech.salessystem.enums.UserType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  TestEntityManager entityManager;

  static User user0;

  @BeforeAll
  static void setup() {
    user0 = new User("Foo Bar Baz", "foobar@email.com",
        "12312312311", UserType.PAYEE, "12345678");
  }


  @Test
  void test_findByEmail_should_return_an_object_optional_with_the_given_email() {
    entityManager.persist(user0);
    var result = userRepository.findByEmail(user0.getEmail());

    assertTrue(result.isPresent());
    assertEquals(result.get().getEmail(), user0.getEmail());
  }

  @Test
  void test_findByEmail_should_return_an_object_optional_empty_when_does_not_exists_the_user_with_the_given_email() {
    var result = userRepository.findByEmail(user0.getEmail());

    assertTrue(result.isEmpty());
  }

  @Test
  void findByCpfCnpj() {
  }
}