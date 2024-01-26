package br.com.giannatech.salessystem.entities.dtos;

public class UserDTO {

  private String fullName;
  private String email;
  private String cpfCnpj;
  private String type;
  private String password;

  public UserDTO() {
  }

  public UserDTO(String fullName, String email, String cpfCnpj, String type, String password) {
    this.fullName = fullName;
    this.email = email;
    this.cpfCnpj = cpfCnpj;
    this.type = type;
    this.password = password;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
