package com.example.tickets.subscription;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final Logger log = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    private final SubscriptionRepository repository;
    private final SubscriptionDTOMapper mapper = SubscriptionDTOMapper.INSTANCE;
    private final Scheduler scheduler;

    public SubscriptionServiceImpl(SubscriptionRepository repository, Scheduler scheduler) {
        this.repository = repository;
        this.scheduler = scheduler;
    }

    @Override
    public Subscription add(String owner, String origin, String destination) {
        boolean exists = repository.exists(owner, origin, destination);
        log.debug("Subscription for '{} {} {}' exists '{}'", owner, origin, destination, exists);

        if (!exists) {
            SubscriptionDTO dto = new SubscriptionDTO(owner, origin, destination);
            Subscription subscription = mapper.dtoToSubscription(dto);
            log.debug("Saving subscription '{}'", subscription);
            repository.save(subscription);
        }

        //TODO
//        Trigger t = TriggerBuilder.newTrigger()
//                .forJob("OnewayTicketsForAYearAviasalesJob")
//                .forJob("LatestTicketsTravelPayoutsPopulationJob")
//                .startNow()
//                .build();
//
//        try {
//            scheduler.scheduleJob(t);
//            log.info("Jobs started");
//
//        } catch (SchedulerException e) {
//            log.error("Failed",e);
//
//        }
        return repository.findBy(owner, origin, destination);
    }

    @Override
    public Subscription get(String owner, String origin, String destination) {
        Subscription subscription = repository.findBy(owner, origin, destination);
        log.debug("Subscription found '{}'", subscription);
        return subscription;
    }

    @Override
    public List<Subscription> get(String owner) {
        List<Subscription> subscriptions = repository.findByOwner(owner);
        log.debug("Subscriptions found '{}'", subscriptions);
        return subscriptions;
    }

    @Override
    public void delete(String owner, String origin, String destination) {
        Subscription subscription = repository.findBy(owner, origin, destination);
        log.debug("Subscription deleted '{}'", subscription);
        repository.delete(subscription);
    }

    @Override
    public void delete(String owner) {
        List<Subscription> subscriptions = repository.findByOwner(owner);
        log.debug("Subscriptions deleted '{}'", subscriptions);
        repository.deleteAll(subscriptions);
    }
}
