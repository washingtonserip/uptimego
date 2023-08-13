package io.uptimego.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Table(name = "subscriptions")
@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date startDate;
    private Date endDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Plan plan;

    @OneToOne(mappedBy = "subscription")
    private Payment payment;
}
