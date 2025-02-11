package com.emna.client_fournisseur.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "_client")
    public class Client {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private String firstName;
        private String lastName;
        @Column(unique = true)
        private String email;
        private String address;
        @Column(unique = true)
        private String number;
    @ElementCollection
    @CollectionTable(name = "client_tel_numbers", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "tel_number")
    private List<Integer> telNumbers;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Integer> getTelNumbers() {
        return telNumbers;
    }

    public void setTelNumbers(List<Integer> telNumbers) {
        this.telNumbers = telNumbers;
    }
}
