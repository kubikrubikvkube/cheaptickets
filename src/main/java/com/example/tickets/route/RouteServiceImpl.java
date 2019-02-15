package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RouteServiceImpl implements RouteService {
    private static final Logger log = LoggerFactory.getLogger(RouteServiceImpl.class);
    private final RouteDtoMapper mapper;
    private final RouteRepository repository;
    private final RoutePlanner routePlanner;
    private final ExampleMatcher exampleMatcher;

    public RouteServiceImpl(RouteDtoMapper mapper, RouteRepository repository, RoutePlanner routePlanner) {
        this.mapper = mapper;
        this.repository = repository;
        this.routePlanner = routePlanner;
        this.exampleMatcher = ExampleMatcher.matchingAll().withIgnorePaths("id", "creationTimestamp").withIncludeNullValues();
    }

    @Override
    public Route save(RouteDto dto) {
        Route route = mapper.fromDto(dto);
        return repository.save(route);
    }

    @Override
    public List<Route> save(List<RouteDto> routeDtos) {
        List<Route> routes = mapper.fromDto(routeDtos);
        Iterable<Route> savedRoutes = repository.saveAll(routes);
        return Lists.newArrayList(savedRoutes);
    }

    @Override
    public List<Route> saveIfNotExist(List<RouteDto> routeDtos) {
        List<Route> routes = mapper.fromDto(routeDtos);
        List<Route> savedRoutes = new ArrayList<>();
        for (Route route : routes) {
            var example = Example.of(route, exampleMatcher);
            var exists = repository.exists(example);
            if (!exists) {
                Route saved = repository.save(route);
                savedRoutes.add(saved);
            }
        }
        return savedRoutes;
    }

    @Override
    public Route saveIfNotExist(RouteDto routeDto) {
        Route route = mapper.fromDto(routeDto);
        var example = Example.of(route, exampleMatcher);
        Optional<Route> found = repository.findOne(example);
        if (found.isEmpty()) {
            return repository.save(route);
        }
        return found.get();

    }

    @Override
    public boolean exist(RouteDto routeDto) {
        Route route = mapper.fromDto(routeDto);
        var example = Example.of(route, exampleMatcher);
        return repository.exists(example);
    }

    @Override
    public List<Route> findBy(String origin, String destination) {
        return repository.findBy(origin, destination);
    }

    @Override
    public List<RouteDto> plan(Subscription subscription) {
        return routePlanner.plan(subscription);
    }

    @Override
    public List<Route> findBy(String origin, String destination, Integer limit) {
        List<Route> routes = this.findBy(origin, destination);
        return routes.stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return repository.count();
    }


}
