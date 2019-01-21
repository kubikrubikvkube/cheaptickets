package com.example.tickets.owner;

import java.util.Optional;

public interface OwnerService {
    Owner add(String name, String email);

    Optional<Owner> get(String name);

    void delete(String name);

    Owner add(String name);

    long countDistinct();
}
