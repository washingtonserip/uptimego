package io.uptimego.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Table(name = "plans")
@Entity
public class Plan {
    @Id
    @Enumerated(EnumType.STRING)
    private PlanSlugs slug;
    private String name; // e.g. "Basic", "Pro", "Enterprise"
    private Double price;
    private String description;

    @OneToMany(mappedBy = "plan")
    private List<Subscription> subscriptions;
}