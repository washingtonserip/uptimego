package io.uptimego.model;

import io.uptimego.util.CryptoConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UptimeConfigOptions {
    private String host;
    private int port;
    private String username;
    @Convert(converter = CryptoConverter.class)
    private String password;
    private String emailTo;
}
