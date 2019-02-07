package com.example.tickets.notification;

import org.springframework.stereotype.Service;

@Service
public class RouteNotificationServiceImpl implements RouteNotificationService {
    private final RouteNotificationRepository repository;
    private final RouteNotificationDtoMapper notificationDtoMapper;


    public RouteNotificationServiceImpl(RouteNotificationRepository repository, RouteNotificationDtoMapper notificationDtoMapper) {
        this.repository = repository;
        this.notificationDtoMapper = notificationDtoMapper;
    }

    @Override
    public RouteNotification save(RouteNotificationDto routeNotificationDto) {
        RouteNotification routeNotification = notificationDtoMapper.fromDto(routeNotificationDto);
        return repository.save(routeNotification);
    }

    @Override
    public RouteNotification save(RouteNotification routeNotification) {
        return repository.save(routeNotification);
    }
}
