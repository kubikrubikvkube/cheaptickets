package com.example.tickets.repository.subscription;

import com.example.tickets.repository.Ticket;
import com.example.tickets.repository.TicketRepository;
import com.example.tickets.service.TicketService;
import com.example.tickets.service.subscription.SubscriptionDTO;
import com.example.tickets.service.travelpayouts.request.LatestRequest;
import lombok.extern.java.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

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
    private ModelMapper mapper;

    @Autowired
    private TicketRepository ticketRepository;
    private SubscriptionDTO subscriptionDTO;
    private Subscription subscription;

    @Before
    public void setUp() throws Exception {
        subscriptionDTO = new SubscriptionDTO(owner, origin, destination);
        var mapped = mapper.map(subscriptionDTO, Subscription.class);
        subscription = subscriptionRepository.save(mapped);
    }

    @After
    public void tearDown() throws Exception {
        subscriptionRepository.delete(subscription);
    }

    @Test
    public void subscriptionCanBeFoundByDifferentWays() {
        Optional<Subscription> byId = subscriptionRepository.findById(subscription.getId());
        assertTrue(byId.isPresent());
        assertEquals(byId.get(), subscription);

        List<Subscription> byOwner = subscriptionRepository.findByOwner(owner);
        assertNotNull(byOwner);
        assertThat(byOwner, hasSize(1));
        assertEquals(byOwner.get(0), subscription);

        List<Subscription> byOrigin = subscriptionRepository.findByOrigin(origin);
        assertNotNull(byOrigin);
        assertThat(byOrigin, hasSize(1));
        assertEquals(byOrigin.get(0), subscription);

        List<Subscription> byDestination = subscriptionRepository.findByDestination(destination);
        assertNotNull(byDestination);
        assertThat(byDestination, hasSize(1));
        assertEquals(byDestination.get(0), subscription);

        List<Subscription> byOriginAndDestination = subscriptionRepository.findByOriginAndDestination(origin, destination);
        assertNotNull(byOriginAndDestination);
        assertThat(byOriginAndDestination, hasSize(1));
        assertEquals(byOriginAndDestination.get(0), subscription);
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

        List<Ticket> byPrice = ticketService.getLatest(someTicketRequest);
        Optional<Ticket> byPriceEntityOpt = byPrice.stream().findFirst();
        assertTrue(byPriceEntityOpt.isPresent());
        Ticket ticket = byPriceEntityOpt.get();
        log.info("Got ticket: " + ticket);
        ticketRepository.save(ticket);
        List<Ticket> bySubscription = ticketRepository.findBySubscription(subscriptionDTO);
        assertNotNull(bySubscription);
        assertThat(bySubscription, hasSize(greaterThanOrEqualTo(1)));
        log.info(String.format("Found %d tickets for subscriptionDTO %s", bySubscription.size(), subscriptionDTO));
        ticketRepository.delete(ticket);
    }
}