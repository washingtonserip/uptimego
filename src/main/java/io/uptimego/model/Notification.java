package io.uptimego.model;

import io.uptimego.enums.ChannelType;
import io.uptimego.enums.NotificationStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Table(name = "notifications")
@Entity
public class Notification {

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(
            name = "tsid",
            strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alert_id")
    private Alert alert;

    @Enumerated(EnumType.STRING)
    private NotificationStatus type;

    @Enumerated(EnumType.STRING)
    private ChannelType channelType;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String message;
}
