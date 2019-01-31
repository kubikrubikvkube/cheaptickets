package com.example.tickets.owner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
@Transactional
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    @Query("select count(o)>0 from Owner o where o.email = ?1")
    boolean existsByEmail(String email);

    @Query("select count(o)>0 from Owner o where o.name = ?1")
    boolean existsByName(String name);

    @Query("select o from Owner o where o.name = ?1")
    Optional<Owner> findBy(String name);
}


