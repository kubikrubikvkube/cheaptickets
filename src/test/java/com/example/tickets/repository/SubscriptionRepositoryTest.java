package com.example.tickets.repository;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionEntity;
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
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class SubscriptionRepositoryTest {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private SubscriptionEntity subscription;

    @Before
    public void createSubscription() {
        Subscription subscription = new Subscription();
        subscription.setOwner("root");
        subscription.setOrigin("LED");
        subscription.setDestination("DME");
        this.subscription = toEntity(subscription);
    }

    @After
    public void deleteSubscription() {
        subscriptionRepository.delete(subscription);
    }

    @Test
    public void shouldBeSavedAndFetchedVariousWays() {
        SubscriptionEntity savedEntity = subscriptionRepository.save(subscription);
        Optional<SubscriptionEntity> byId = subscriptionRepository.findById(savedEntity.getId());
        assertTrue(byId.isPresent());
        assertEquals(byId.get().getOwner(), "root");
        assertEquals(byId.get().getOrigin(), "LED");
        assertEquals(byId.get().getDestination(), "DME");
        List<SubscriptionEntity> byOwner = subscriptionRepository.findByOwner("root");
        assertNotNull(byOwner);
        assertThat(byOwner, hasSize(1));
        assertEquals(byOwner.get(0).getOwner(), "root");
        assertEquals(byOwner.get(0).getOrigin(), "LED");
        assertEquals(byOwner.get(0).getDestination(), "DME");
        List<SubscriptionEntity> byOrigin = subscriptionRepository.findByOrigin("LED");
        assertNotNull(byOrigin);
        assertThat(byOrigin, hasSize(1));
        assertEquals(byOrigin.get(0).getOwner(), "root");
        assertEquals(byOrigin.get(0).getOrigin(), "LED");
        assertEquals(byOrigin.get(0).getDestination(), "DME");
        List<SubscriptionEntity> byDestination = subscriptionRepository.findByDestination("DME");
        assertNotNull(byDestination);
        assertThat(byDestination, hasSize(1));
        assertEquals(byDestination.get(0).getOwner(), "root");
        assertEquals(byDestination.get(0).getOrigin(), "LED");
        assertEquals(byDestination.get(0).getDestination(), "DME");
        List<SubscriptionEntity> byOriginAndDestination = subscriptionRepository.findByOriginAndDestination("LED", "DME");
        assertNotNull(byOriginAndDestination);
        assertThat(byOriginAndDestination, hasSize(1));
        assertEquals(byOriginAndDestination.get(0).getOwner(), "root");
        assertEquals(byOriginAndDestination.get(0).getOrigin(), "LED");
        assertEquals(byOriginAndDestination.get(0).getDestination(), "DME");
    }


}