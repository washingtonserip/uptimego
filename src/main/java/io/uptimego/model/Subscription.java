package io.uptimego.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Table(name = "subscriptions")
@Entity
public class Subscription {

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(
            name = "tsid",
            strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator"
    )
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
