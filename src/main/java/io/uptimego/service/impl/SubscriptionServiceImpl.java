package io.uptimego.service.impl;

import io.uptimego.model.Plan;
import io.uptimego.model.PlanSlug;
import io.uptimego.model.Subscription;
import io.uptimego.model.User;
import io.uptimego.repository.PlanRepository;
import io.uptimego.repository.SubscriptionRepository;
import io.uptimego.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;

    @Override
    @Transactional
    public Subscription createBasicSubscription(User user) {
        Plan plan = planRepository.findBySlug(PlanSlug.BASIC);
        Subscription subscription = new Subscription();
        subscription.setStartDate(new Date());
        subscription.setUser(user);
        subscription.setPlan(plan);

        // Set end date to one year from now
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        subscription.setEndDate(cal.getTime());

        subscriptionRepository.save(subscription);

        return subscription;
    }
}
