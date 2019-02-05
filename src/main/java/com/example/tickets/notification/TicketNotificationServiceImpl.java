package com.example.tickets.notification;

import org.springframework.stereotype.Service;

@Service
public class TicketNotificationServiceImpl implements TicketNotificationService {
    private final TicketNotificationRepository repository;
    private final TicketNotificationDTOMapper notificationDTOMapper;


    public TicketNotificationServiceImpl(TicketNotificationRepository repository, TicketNotificationDTOMapper notificationDTOMapper) {
        this.repository = repository;
        this.notificationDTOMapper = notificationDTOMapper;
    }

    @Override
    public TicketNotification save(TicketNotificationDTO ticketNotificationDTO) {
        TicketNotification ticketNotification = notificationDTOMapper.fromDTO(ticketNotificationDTO);
        return repository.save(ticketNotification);
    }
}
