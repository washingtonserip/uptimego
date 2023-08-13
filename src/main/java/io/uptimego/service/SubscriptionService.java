package io.uptimego.service;

import io.uptimego.model.Subscription;
import io.uptimego.model.User;

public interface SubscriptionService {
    Subscription createBasicSubscription(User user);
}
