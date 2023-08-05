package io.uptimego.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // create email, name, password and role fields
    private String email;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private ClientRole clientRole;

    // Other necessary fields
}

