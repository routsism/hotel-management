package gr.aueb.cf.hotel_managment.model;

import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotels")
public class Hotel extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    private String phone;

    private String email;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Room> rooms = new HashSet<>();

}
