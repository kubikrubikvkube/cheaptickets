package com.example.tickets.subscription;

import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerRepository;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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
    private final OwnerRepository ownerRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, Scheduler scheduler, OwnerRepository ownerRepository) {
        this.repository = subscriptionRepository;
        this.scheduler = scheduler;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public List<Subscription> add(String ownerName, String origin, String destination) {
        boolean exists = repository.exists(ownerName, origin, destination);
        log.debug("Subscription for '{} {} {}' exists '{}'", ownerName, origin, destination, exists);
        if (!exists) {
            Owner owner = ownerRepository.findBy(ownerName);
            SubscriptionDTO dto = new SubscriptionDTO();
            dto.setOwner(owner);
            dto.setOrigin(origin);
            dto.setDestination(destination);
            Subscription createdSubscription = mapper.fromDTO(dto);
            log.debug("Saving subscription '{}'", createdSubscription);
            repository.save(createdSubscription);
        }
        return repository.findBy(ownerName, origin, destination);
    }

    @Override
    public List<Subscription> get(String owner, String origin, String destination) {
        List<Subscription> subscription = repository.findBy(owner, origin, destination);
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
        List<Subscription> subscription = repository.findBy(owner, origin, destination);
        log.debug("Subscription deleted '{}'", subscription);
        repository.deleteAll(subscription);
    }

    @Override
    public void delete(String owner) {
        List<Subscription> subscriptions = repository.findByOwnerName(owner);
        log.debug("Subscriptions deleted '{}'", subscriptions);
        repository.deleteAll(subscriptions);
    }

    @Override
    public List<Subscription> add(String ownerName, String origin, String destination, String tripDurationInDays) {
        Integer tripDuration = Integer.parseInt(tripDurationInDays);
        boolean exists = repository.exists(ownerName, origin, destination, tripDuration);
        log.debug("Subscription for '{} {} {} {}' exists '{}'", ownerName, origin, destination, tripDuration, exists);
        if (!exists) {
            Owner owner = ownerRepository.findBy(ownerName);
            SubscriptionDTO dto = new SubscriptionDTO();
            dto.setOwner(owner);
            dto.setOrigin(origin);
            dto.setDestination(destination);
            dto.setTripDurationInDays(tripDuration);
            Subscription createdSubscription = mapper.fromDTO(dto);
            log.debug("Saving subscription '{}'", createdSubscription);
            repository.save(createdSubscription);
        }
        return repository.findBy(ownerName, origin, destination, tripDuration);
    }

    @Override
    public Multimap<String, String> findDistinctOriginAndDestination() {
        Multimap<String, String> map = ArrayListMultimap.create();
        List<Object[]> distinctOriginAndDestination = repository.findDistinctOriginAndDestination();
        for (Object[] originAndDestination : distinctOriginAndDestination) {
            map.put(String.valueOf(originAndDestination[0]), String.valueOf(originAndDestination[1]));
        }
        return map;
    }
}
