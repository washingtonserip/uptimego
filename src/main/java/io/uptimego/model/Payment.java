package io.uptimego.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Table(name = "payments")
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private Date paymentDate;
    private String paymentMethod; // e.g. "Credit Card", "PayPal"

    @OneToOne
    private Subscription subscription;
}
