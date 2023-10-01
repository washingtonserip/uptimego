package io.uptimego.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@Table(name = "payments")
@Entity
public class Payment {

    @Id
    @Tsid
    private Long id;
    private Double amount;
    private Date paymentDate;
    private String paymentMethod; // e.g. "Credit Card", "PayPal"

    @OneToOne
    private Subscription subscription;
}
