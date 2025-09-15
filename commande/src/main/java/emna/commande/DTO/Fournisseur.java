package emna.commande.DTO;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data

@Table(name = "_fournisseur")
public class Fournisseur {
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String address;
    private List<Long> telNumbers;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Long> getTelNumbers() {
        return telNumbers;
    }

    public void setTelNumbers(List<Long> telNumbers) {
        this.telNumbers = telNumbers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
