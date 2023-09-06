package io.uptimego.service;

import io.uptimego.model.Pulse;

import java.util.List;

public interface PulseService {

    Pulse save(Pulse pulse);

    List<Pulse> findByCurrentUser();

}
