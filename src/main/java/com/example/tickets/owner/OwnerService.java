package com.example.tickets.owner;

import java.util.List;
import java.util.Optional;

public interface OwnerService {
    Owner add(String name, String email);

    Owner save(OwnerDTO dto);

    Optional<Owner> find(String name);

    List<Owner> findAll();

    void delete(String name);

    void delete(Long id);

    Owner add(String name);

    long countDistinct();
}
