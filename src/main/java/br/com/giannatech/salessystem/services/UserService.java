package br.com.giannatech.salessystem.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.giannatech.salessystem.entities.User;
import br.com.giannatech.salessystem.entities.dtos.UserDTO;
import br.com.giannatech.salessystem.exceptions.InvalidParamsException;
import br.com.giannatech.salessystem.exceptions.UserNotFoundException;
import br.com.giannatech.salessystem.repositories.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public UserService(UserRepository userRepository, ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
  }

  public User create(UserDTO userDTO) {

    userRepository.findByEmail(userDTO.getEmail())
        .ifPresent(user -> {
          throw new InvalidParamsException("Email already exists");
        });

    userRepository.findByCpfCnpj(userDTO.getCpfCnpj())
        .ifPresent(user -> {
          throw new InvalidParamsException("CPF/CNPJ already exists");
        });

    var user = modelMapper.map(userDTO, User.class);

    return userRepository.save(user);
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public void deleteById(Long id) {
    var user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found"));

    userRepository.delete(user);
  }

  public User findById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
  }

}
