package io.uptimego.model;


import io.uptimego.enums.ChannelType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;

@Getter
@Setter
@Table(name = "channels")
@Entity
public class Channel {

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(
            name = "tsid",
            strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    private ChannelMetadata metadata;
}
