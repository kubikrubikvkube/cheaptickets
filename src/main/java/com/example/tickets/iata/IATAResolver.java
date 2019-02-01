package com.example.tickets.iata;

public interface IATAResolver {
    /**
     * Returns IATA code for this name
     *
     * @param place
     * @return
     */
    String resolve(String place);
}
