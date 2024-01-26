package br.com.giannatech.salessystem.entities;

import br.com.giannatech.salessystem.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(name = "cpf_cnpj", unique = true)
  private String cpfCnpj;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserType type;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  public User() {
  }

  public User(Long id, String fullName, String email, String cpfCnpj, UserType type, String password) {
    this.id = id;
    this.fullName = fullName;
    this.email = email;
    this.cpfCnpj = cpfCnpj;
    this.type = type;
    this.password = password;
  }

  public User(String fullName, String email, String cpfCnpj, UserType type, String password) {
    this.fullName = fullName;
    this.email = email;
    this.cpfCnpj = cpfCnpj;
    this.type = type;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCpfCnpj() {
    return cpfCnpj;
  }

  public void setCpfCnpj(String cpfCnpj) {
    this.cpfCnpj = cpfCnpj;
  }

  public UserType getType() {
    return type;
  }

  public void setType(UserType type) {
    this.type = type;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
