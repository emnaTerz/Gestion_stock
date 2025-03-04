package com.emna.client_fournisseur.dao.Request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

public class ClientRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String number;
    private List<Integer> telNumbers;

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
