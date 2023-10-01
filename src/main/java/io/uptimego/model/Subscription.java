package io.uptimego.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import io.uptimego.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@Table(name = "subscriptions")
@Entity
public class Subscription {

    @Id
    @Tsid
    private Long id;
    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @ManyToOne
    private User user;

    @ManyToOne
    private Plan plan;

    @OneToOne(mappedBy = "subscription")
    private Payment payment;
}
