package com.example.tickets.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
interface RouteNotificationRepository extends JpaRepository<RouteNotification, Long> {
}
