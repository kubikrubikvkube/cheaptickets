package com.example.tickets.owner;

import java.util.List;
import java.util.Optional;

public interface OwnerService {

    Owner save(OwnerDto dto);

    Owner save(Owner owner);

    Optional<Owner> find(Long id);

    Optional<Owner> find(String email);

    List<Owner> findAll();

    void delete(String email);

    void delete(Long id);

    Owner add(String email);

    long count();
}
