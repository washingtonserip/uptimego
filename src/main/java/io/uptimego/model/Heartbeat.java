package io.uptimego.model;

import io.uptimego.util.JsonbConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Heartbeat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String url;

    @Enumerated(EnumType.STRING)
    private HeartbeatType type;


    @Convert(converter = JsonbConverter.class)
    private HeartbeatOptions options;
}

