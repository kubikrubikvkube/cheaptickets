package com.example.tickets.subscription;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.example.tickets.subscription.SubscriptionType.*;

@Component
public class SubscriptionTypeResolverImpl implements SubscriptionTypeResolver {
    private final Logger log = LoggerFactory.getLogger(SubscriptionTypeResolverImpl.class);
    private final SubscriptionDtoMapper mapper;

    public SubscriptionTypeResolverImpl(SubscriptionDtoMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public SubscriptionType resolve(Subscription subscription) {
        if (subscription.getSubscriptionType() != null) return subscription.getSubscriptionType();
        SubscriptionDto dto = mapper.toDto(subscription);
        return resolve(dto);
    }

    @Override
    public SubscriptionType resolve(SubscriptionDto dto) {
        if (dto.getSubscriptionType() != null) return dto.getSubscriptionType();
        var owner = dto.getOwner();
        var origin = dto.getOrigin();
        var destination = dto.getDestination();
        var departDate = dto.getDepartDate();
        var returnDate = dto.getReturnDate();
        var tripDurationFrom = dto.getTripDurationInDaysFrom();
        var tripDurationTo = dto.getTripDurationInDaysTo();

        if (owner == null || origin == null) {
            log.error("Owner {} or origin {} is invalid in {}", owner, origin, dto);
            return INVALID;
        }
        SubscriptionType subscriptionType = null;
        if (isNotNull(destination) && isNull(departDate, returnDate, tripDurationFrom, tripDurationTo)) {
            subscriptionType = DESTINATION;
        }
        if (isNotNull(destination, departDate) && isNull(returnDate, tripDurationFrom, tripDurationTo)) {
            subscriptionType = DESTINATION_DEPART_DATE;
        }

        if (isNotNull(departDate, returnDate) && isNull(destination, tripDurationFrom, tripDurationTo)) {
            subscriptionType = DEPART_DATE_AND_RETURN_DATE;
        }

        if (isNotNull(destination, tripDurationTo) && isNull(departDate, tripDurationFrom, returnDate)) {
            subscriptionType = DESTINATION_TRIP_DURATION_TO;
        }

        if (isNotNull(destination, tripDurationFrom) && isNull(departDate, returnDate, tripDurationTo)) {
            subscriptionType = DESTINATION_TRIP_DURATION_FROM;
        }

        if (isNotNull(destination, departDate, tripDurationFrom) && isNull(returnDate, tripDurationTo)) {
            subscriptionType = DESTINATION_DEPART_DATE_TRIP_DURATION_FROM;
        }

        if (isNotNull(destination, tripDurationFrom, tripDurationTo) && isNull(departDate, returnDate)) {
            subscriptionType = DESTINATION_TRIP_DURATION_FROM_TRIP_DURATION_TO;
        }

        if (isNotNull(tripDurationFrom, tripDurationTo) && isNull(destination, departDate, returnDate)) {
            subscriptionType = TRIP_DURATION_FROM_TRIP_DURATION_TO;
        }

        if (subscriptionType == null) {
            subscriptionType = INVALID;
            log.error("SubscriptionType resolve error {} for {}", subscriptionType, dto);
            return subscriptionType;
        }

        log.debug("SubscriptionType resolved {} for {}", subscriptionType, dto);
        return subscriptionType;
    }

    private boolean isNotNull(Object... objects) {
        return Lists.newArrayList(objects).stream().allMatch(Objects::nonNull);
    }

    private boolean isNull(Object... objects) {
        return Lists.newArrayList(objects).stream().allMatch(Objects::isNull);
    }


}
