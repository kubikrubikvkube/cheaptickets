package com.example.tickets.repository.subscription;

import com.example.tickets.service.subscription.Subscription;
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
import static junit.framework.TestCase.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class SubscriptionRepositoryTest {
    private final String owner = "root";
    private final String origin = "LED";
    private final String destination = "MOW";
    @Autowired
    SubscriptionRepository subscriptionRepository;
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
}