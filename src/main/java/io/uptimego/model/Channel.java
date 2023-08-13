package io.uptimego.model;


import javax.persistence.*;

@Entity
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long channelId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String channelType;

    @Column(columnDefinition = "jsonb")
    private String metadata; // you can also use a custom class if you have a fixed schema

    // Getters and setters
}