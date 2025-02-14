package com.emna.client_fournisseur.entites;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_fournisseur")
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String address;
    @ElementCollection
    @CollectionTable(name = "fournisseur_tel_numbers", joinColumns = @JoinColumn(name = "fournisseur_id"))
    @Column(name = "tel_number")
    private List<Long> telNumbers;


}
