package com.example.tickets.owner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


/**
 * Репозиторий для Owner'ов
 */
@Repository
@Transactional
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    /**
     * Проверяет наличие владельца подписки, зарегистрированного с заранее заданным e-mail адресом
     *
     * @param email почтовый ящик для проверки
     * @return существует ли владелец с таким ящиком
     */
    @Query("select count(o)>0 from Owner o where o.email = ?1")
    boolean existsByEmail(String email);

    /**
     * Пытается найти владельца подписки, который зарегистрирован с определённым e-mail адресом
     *
     * @param email почтовый ящик для проверки
     * @return возвращает владельца с заданным почтовым ящиком, если он найден
     */
    @Query("select o from Owner o where o.email = ?1")
    Optional<Owner> findBy(String email);
}


