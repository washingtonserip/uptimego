package io.uptimego.service;

import io.uptimego.model.Alert;
import io.uptimego.model.Pulse;

public interface AlertService {
    Alert createNonDuplicatedAlert(Pulse pulse);

    Alert createAlert(Pulse pulse);
}
