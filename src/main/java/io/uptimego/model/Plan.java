package io.uptimego.model;

import io.uptimego.enums.PlanSlug;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Table(name = "plans")
@Entity
public class Plan {
    @Id
    @Enumerated(EnumType.STRING)
    private PlanSlug slug;
    private String name; // e.g. "Basic", "Pro", "Enterprise"
    private Double price;
    private String description;

    @OneToMany(mappedBy = "plan")
    private List<Subscription> subscriptions;
}
