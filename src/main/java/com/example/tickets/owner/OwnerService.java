package com.example.tickets.owner;

public interface OwnerService {
    Owner add(String name, String email);

    Owner get(String name);

    void delete(String name);

}
