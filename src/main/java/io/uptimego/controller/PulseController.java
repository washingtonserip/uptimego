package io.uptimego.controller;

import io.uptimego.model.Pulse;
import io.uptimego.service.PulseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pulses")
public class PulseController {

    private final PulseService service;

    @GetMapping
    public List<Pulse> getAll() {
        return service.findByCurrentUser();
    }

}
