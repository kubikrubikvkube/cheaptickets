package com.example.tickets.repository.subscription;

import com.example.tickets.repository.TicketEntity;
import com.example.tickets.repository.TicketRepository;
import com.example.tickets.repository.util.EntityConverter;
import com.example.tickets.service.TicketJson;
import com.example.tickets.service.TicketService;
import com.example.tickets.service.subscription.Subscription;
import com.example.tickets.service.travelpayouts.request.LatestRequest;
import lombok.extern.java.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.example.tickets.repository.util.EntityConverter.toEntity;
import static com.example.tickets.service.travelpayouts.request.Sorting.DISTANCE_UNIT_PRICE;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class SubscriptionRepositoryTest {
    private final String owner = "root";
    private final String origin = "LED";
    private final String destination = "MOW";
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;
    private Subscription subscription;
    private SubscriptionEntity subscriptionEntity;
    private SubscriptionEntity save;

    @Before
    public void setUp() throws Exception {
        subscription = new Subscription(owner, origin, destination);
        subscriptionEntity = toEntity(subscription);
        save = subscriptionRepository.save(subscriptionEntity);
    }

    @After
    public void tearDown() throws Exception {
        subscriptionRepository.delete(subscriptionEntity);
    }

    @Test
    public void subscriptionCanBeFoundByDifferentWays() {
        assertEquals(subscriptionEntity, save);
        Optional<SubscriptionEntity> byId = subscriptionRepository.findById(subscriptionEntity.getId());
        assertTrue(byId.isPresent());
        assertEquals(byId.get(), subscriptionEntity);

        List<SubscriptionEntity> byOwner = subscriptionRepository.findByOwner(owner);
        assertNotNull(byOwner);
        assertThat(byOwner, hasSize(1));
        assertEquals(byOwner.get(0), subscriptionEntity);

        List<SubscriptionEntity> byOrigin = subscriptionRepository.findByOrigin(origin);
        assertNotNull(byOrigin);
        assertThat(byOrigin, hasSize(1));
        assertEquals(byOrigin.get(0), subscriptionEntity);

        List<SubscriptionEntity> byDestination = subscriptionRepository.findByDestination(destination);
        assertNotNull(byDestination);
        assertThat(byDestination, hasSize(1));
        assertEquals(byDestination.get(0), subscriptionEntity);

        List<SubscriptionEntity> byOriginAndDestination = subscriptionRepository.findByOriginAndDestination(origin, destination);
        assertNotNull(byOriginAndDestination);
        assertThat(byOriginAndDestination, hasSize(1));
        assertEquals(byOriginAndDestination.get(0), subscriptionEntity);

    }

    @Test
    public void ticketsShouldBeFoundBySubscription() {
        LatestRequest someTicketRequest = LatestRequest.builder()
                .origin(origin)
                .destination(destination)
                .period_type("year")
                .sorting(DISTANCE_UNIT_PRICE)
                .show_to_affiliates(false)
                .limit(1)
                .build();

        List<TicketJson> byPrice = ticketService.getLatest(someTicketRequest);
        Optional<TicketEntity> byPriceEntityOpt = byPrice.stream().map(EntityConverter::toEntity).findFirst();
        assertTrue(byPriceEntityOpt.isPresent());
        TicketEntity ticketEntity = byPriceEntityOpt.get();
        log.info("Got ticket: " + ticketEntity);
        ticketRepository.save(ticketEntity);
        List<TicketEntity> bySubscription = ticketRepository.findBySubscription(subscription);
        assertNotNull(bySubscription);
        assertThat(bySubscription, hasSize(greaterThanOrEqualTo(1)));
        log.info(String.format("Found %d tickets for subscription %s", bySubscription.size(), subscription));
        ticketRepository.delete(ticketEntity);
    }
}