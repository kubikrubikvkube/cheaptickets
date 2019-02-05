package com.example.tickets.notification;

import org.springframework.stereotype.Service;

@Service
public class RouteNotificationServiceImpl implements RouteNotificationService {
    private final RouteNotificationRepository repository;
    private final RouteNotificationDTOMapper notificationDTOMapper;


    public RouteNotificationServiceImpl(RouteNotificationRepository repository, RouteNotificationDTOMapper notificationDTOMapper) {
        this.repository = repository;
        this.notificationDTOMapper = notificationDTOMapper;
    }

    @Override
    public RouteNotification save(RouteNotificationDTO routeNotificationDTO) {
        RouteNotification routeNotification = notificationDTOMapper.fromDTO(routeNotificationDTO);
        return repository.save(routeNotification);
    }

    @Override
    public RouteNotification save(RouteNotification routeNotification) {
        return repository.save(routeNotification);
    }
}
