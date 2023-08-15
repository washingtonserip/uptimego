package io.uptimego.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.uptimego.enums.TargetType;
import io.uptimego.service.EntityChangesListener;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "targets")
@EntityListeners(EntityChangesListener.class)
@Entity
public class Target {

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(
            name = "tsid",
            strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator"
    )
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String url;

    @Enumerated(EnumType.STRING)
    private TargetType type;

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    private TargetOptions options;
}
