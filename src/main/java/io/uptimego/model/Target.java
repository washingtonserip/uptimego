package io.uptimego.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.id.Tsid;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import io.uptimego.enums.TargetType;
import io.uptimego.service.EntityChangesListener;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;

@Getter
@Setter
@Table(name = "targets")
@EntityListeners(EntityChangesListener.class)
@Entity
public class Target {

    @Id
    @Tsid
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String url;

    @Enumerated(EnumType.STRING)
    private TargetType type;

    @Type(JsonType.class)
    private TargetOptions options;
}
