package com.example.tickets.subscription;

import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerRepository;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final Logger log = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    private final SubscriptionRepository repository;
    private final SubscriptionMapper mapper = SubscriptionMapper.INSTANCE;
    private final Scheduler scheduler;
    private final OwnerRepository ownerRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, Scheduler scheduler, OwnerRepository ownerRepository) {
        this.repository = subscriptionRepository;
        this.scheduler = scheduler;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Subscription add(String ownerName, String origin, String destination) {
        boolean exists = repository.exists(ownerName, origin, destination);
        log.debug("Subscription for '{} {} {}' exists '{}'", ownerName, origin, destination, exists);
        if (!exists) {
            Owner owner = ownerRepository.findBy(ownerName);
            SubscriptionDTO dto = new SubscriptionDTO();
            dto.setOwner(owner);
            dto.setOrigin(origin);
            dto.setDestination(destination);
            Subscription createdSubscription = mapper.dtoToSubscription(dto);
            log.debug("Saving subscription '{}'", createdSubscription);
            repository.save(createdSubscription);
//            log.debug("Saving owner '{}'", owner);
// TODO bidirectional owner <-> subscription
//            List<Subscription> ownerSubscriptions = owner.getSubscriptions();
//            ownerSubscriptions.add(createdSubscription);
//            owner.setSubscriptions(ownerSubscriptions);
//            ownerRepository.save(owner);

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
        return repository.findBy(ownerName, origin, destination);
    }

    @Override
    public Subscription get(String owner, String origin, String destination) {
        Subscription subscription = repository.findBy(owner, origin, destination);
        log.debug("Subscription found '{}'", subscription);
        return subscription;
    }

    @Override
    public List<Subscription> get(String owner) {
        List<Subscription> subscriptions = repository.findByOwnerName(owner);
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
        List<Subscription> subscriptions = repository.findByOwnerName(owner);
        log.debug("Subscriptions deleted '{}'", subscriptions);
        repository.deleteAll(subscriptions);
    }
}
