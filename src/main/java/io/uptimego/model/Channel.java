package io.uptimego.model;


import io.hypersistence.utils.hibernate.id.Tsid;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import io.uptimego.enums.ChannelType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;

@Getter
@Setter
@Table(name = "channels")
@Entity
public class Channel {

    @Id
    @Tsid
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private ChannelMetadata metadata;
}
