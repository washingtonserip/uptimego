package io.uptimego.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Table(name = "payments")
@Entity
public class Payment {

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(
            name = "tsid",
            strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator"
    )
    private Long id;
    private Double amount;
    private Date paymentDate;
    private String paymentMethod; // e.g. "Credit Card", "PayPal"

    @OneToOne
    private Subscription subscription;
}
