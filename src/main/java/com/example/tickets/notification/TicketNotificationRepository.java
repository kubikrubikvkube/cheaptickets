package com.example.tickets.notification;

import org.springframework.data.jpa.repository.JpaRepository;

interface TicketNotificationRepository extends JpaRepository<TicketNotification, Long> {
}
