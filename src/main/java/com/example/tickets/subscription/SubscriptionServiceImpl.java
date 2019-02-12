package com.example.tickets.subscription;

import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerRepository;
import com.example.tickets.util.ServiceException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    private final SubscriptionRepository repository;
    private final SubscriptionDtoMapper mapper;
    private final OwnerRepository ownerRepository;
    private final ExampleMatcher exampleMatcher;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, OwnerRepository ownerRepository, SubscriptionDtoMapper mapper) {
        this.repository = subscriptionRepository;
        this.ownerRepository = ownerRepository;
        this.mapper = mapper;
        this.exampleMatcher = ExampleMatcher.matchingAll().withIgnorePaths("id", "creationTimestamp", "foundAt").withIncludeNullValues();
    }

    @Override
    public List<Subscription> add(String ownerName, String origin, String destination) {
        boolean exists = repository.exists(ownerName, origin, destination);
        log.debug("Subscription for '{} {} {}' exists '{}'", ownerName, origin, destination, exists);
        if (!exists) {
            Optional<Owner> ownerOpt = ownerRepository.findBy(ownerName);
            if (ownerOpt.isPresent()) {
                Owner owner = ownerOpt.get();
                SubscriptionDto dto = new SubscriptionDto();
                dto.setOwner(owner);
                dto.setOrigin(origin);
                dto.setDestination(destination);
                Subscription createdSubscription = mapper.fromDto(dto);
                log.debug("Saving subscription '{}'", createdSubscription);
                repository.save(createdSubscription);
            } else {
                var msg = String.format("Owner %s does not exist", ownerName);
                throw new ServiceException(msg);
            }

        }
        return repository.findBy(ownerName, origin, destination);
    }

    @Override
    public List<Subscription> add(String ownerName, String origin, String destination, String departDate, String returnDate) {
        LocalDate localDepartDate = LocalDate.parse(departDate);
        LocalDate localReturnDate = LocalDate.parse(returnDate);
        boolean exists = repository.exists(ownerName, origin, destination, localDepartDate, localReturnDate);
        log.debug("Subscription for '{} {} {} {} {}' exists '{}'", ownerName, origin, destination, departDate, returnDate, exists);
        if (!exists) {
            Optional<Owner> ownerOpt = ownerRepository.findBy(ownerName);
            if (ownerOpt.isPresent()) {
                var tripDuration = localDepartDate.until(localReturnDate).getDays();
                Owner owner = ownerOpt.get();
                SubscriptionDto dto = new SubscriptionDto();
                dto.setOwner(owner);
                dto.setOrigin(origin);
                dto.setDestination(destination);
                dto.setDepartDate(localDepartDate);
                dto.setReturnDate(localReturnDate);
                dto.setTripDurationInDaysFrom(tripDuration);
                dto.setTripDurationInDaysTo(tripDuration);
                Subscription createdSubscription = mapper.fromDto(dto);
                log.info("Saving subscription '{}'", createdSubscription);
                repository.save(createdSubscription);
            } else {
                var msg = String.format("Owner %s does not exist", ownerName);
                throw new ServiceException(msg);
            }

        }
        return repository.findBy(ownerName, origin, destination, LocalDate.parse(departDate), LocalDate.parse(returnDate));
    }

    @Override
    public List<Subscription> get(String origin, String destination) {
        return repository.findBy(origin, destination);
    }

    @Override
    public List<Subscription> get(String owner, String origin, String destination) {
        List<Subscription> subscription = repository.findBy(owner, origin, destination);
        log.debug("Subscription found '{}'", subscription);
        return subscription;
    }

    @Override
    public List<Subscription> get(String ownerEmail) {
        List<Subscription> subscriptions = repository.findByOwnerEmail(ownerEmail);
        log.debug("Subscriptions found '{}'", subscriptions);
        return subscriptions;
    }

    @Override
    public Optional<Subscription> find(SubscriptionDto dto) {
        Subscription subscription = mapper.fromDto(dto);
        Example<Subscription> probe = Example.of(subscription, exampleMatcher);
        if (repository.exists(probe)) {
            return repository.findOne(probe);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Subscription> find(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Subscription> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Override
    public void delete(String owner, String origin, String destination) {
        List<Subscription> subscription = repository.findBy(owner, origin, destination);
        log.debug("Subscription deleted '{}'", subscription);
        repository.deleteAll(subscription);
    }

    @Override
    public void delete(String owner) {
        List<Subscription> subscriptions = repository.findByOwnerEmail(owner);
        log.debug("Subscriptions deleted '{}'", subscriptions);
        repository.deleteAll(subscriptions);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

//    @Override
//    public Subscription add(String ownerName, String origin, String destination, String tripDurationInDays) {
//        Integer tripDuration = Integer.parseInt(tripDurationInDays);
//        boolean exists = repository.exists(ownerName, origin, destination, tripDuration);
//        log.info("Subscription for '{} {} {} {}' exists '{}'", ownerName, origin, destination, tripDuration, exists);
//        if (!exists) {
//            Optional<Owner> ownerOpt = ownerRepository.findBy(ownerName);
//            if (ownerOpt.isPresent()) {
//                Owner owner = ownerOpt.get();
//                SubscriptionDto dto = new SubscriptionDto();
//                dto.setOwner(owner);
//                dto.setOrigin(origin);
//                dto.setDestination(destination);
//                dto.setTripDurationInDaysFrom(tripDuration);
//                dto.setTripDurationInDaysTo(tripDuration);
//                Subscription createdSubscription = mapper.fromDto(dto);
//                log.debug("Saving subscription '{}'", createdSubscription);
//                repository.save(createdSubscription);
//            } else {
//                var msg = String.format("Owner %s does not exist", ownerName);
//                throw new ServiceException(msg);
//            }
//
//
//        }
//        return repository.findBy(ownerName, origin, destination, tripDuration);
//    }

    @Override
    public Multimap<String, String> findDistinctOriginAndDestination() {
        Multimap<String, String> map = ArrayListMultimap.create();
        List<Object[]> distinctOriginAndDestination = repository.findDistinctOriginAndDestination();
        for (Object[] originAndDestination : distinctOriginAndDestination) {
            map.put(String.valueOf(originAndDestination[0]), String.valueOf(originAndDestination[1]));
        }
        return map;
    }


    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Subscription save(SubscriptionDto subscriptionDto) {
        Subscription subscription = mapper.fromDto(subscriptionDto);
        return repository.save(subscription);
    }

    @Override
    public Subscription save(Subscription subscription) {
        return repository.save(subscription);
    }
}
