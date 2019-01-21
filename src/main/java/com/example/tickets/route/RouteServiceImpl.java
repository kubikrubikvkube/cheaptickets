package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RouteServiceImpl implements RouteService {
    private final Logger log = LoggerFactory.getLogger(RouteServiceImpl.class);
    private final RouteDTOMapper mapper;
    private final RouteRepository repository;
    private final RoutePlanner routePlanner;

    public RouteServiceImpl(RouteDTOMapper mapper, RouteRepository repository, RoutePlanner routePlanner) {
        this.mapper = mapper;
        this.repository = repository;
        this.routePlanner = routePlanner;
    }

    @Override
    public Route save(RouteDTO dto) {
        Route route = mapper.fromDTO(dto);
        return repository.save(route);
    }

    @Override
    public List<Route> save(List<RouteDTO> routeDTOS) {
        List<Route> routes = mapper.fromDTO(routeDTOS);
        Iterable<Route> savedRoutes = repository.saveAll(routes);
        return Lists.newArrayList(savedRoutes);
    }

    @Override
    public List<RouteDTO> plan(Subscription subscription) {
        return routePlanner.plan(subscription);
    }
}
