package io.uptimego.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.uptimego.service.EntityChangesListener;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@EntityListeners(EntityChangesListener.class)
@Getter
@Setter
public class UptimeConfig {

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(
            name = "tsid",
            strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator"
    )
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String url;

    @Enumerated(EnumType.STRING)
    private UptimeConfigType type;

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    private UptimeConfigOptions options;
}
