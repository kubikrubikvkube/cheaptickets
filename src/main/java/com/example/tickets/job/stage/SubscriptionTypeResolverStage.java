package com.example.tickets.job.stage;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.subscription.SubscriptionType;
import com.example.tickets.subscription.SubscriptionTypeResolver;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SubscriptionTypeResolverStage implements AbstractStage {
    private final Logger log = LoggerFactory.getLogger(SubscriptionTypeResolverStage.class);
    private final SubscriptionService subscriptionService;
    private final SubscriptionTypeResolver typeResolver;

    public SubscriptionTypeResolverStage(SubscriptionService subscriptionService, SubscriptionTypeResolver typeResolver) {
        this.subscriptionService = subscriptionService;
        this.typeResolver = typeResolver;
    }

    @Override
    public StageResult call() {
        Stopwatch timer = Stopwatch.createStarted();
        log.info("SubscriptionTypeResolverStage started");

        List<Subscription> allSubscriptions = subscriptionService.findAll();

        AtomicInteger counter = new AtomicInteger();
        allSubscriptions
                .stream()
                .filter(s -> s.getSubscriptionType() == null)
                .forEach(subscription -> {
                    SubscriptionType type = typeResolver.resolve(subscription);
                    subscription.setSubscriptionType(type);
                    subscriptionService.save(subscription);
                    counter.incrementAndGet();
                });

        log.info("SubscriptionTypeResolverStage finished in {}", timer.stop());
        return new StageResult("SubscriptionTypeResolverStage", 0, counter.get(), 0);
    }
}
