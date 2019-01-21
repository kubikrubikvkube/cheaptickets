package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RouteServiceImpl implements RouteService {
    private final Logger log = LoggerFactory.getLogger(RouteServiceImpl.class);

    Route plan(Subscription subscription) {
        String origin = subscription.getOrigin();
        String destination = subscription.getDestination();

        return null;
    }
}
