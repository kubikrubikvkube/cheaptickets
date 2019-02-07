package com.example.tickets.travelpayouts;

import com.example.tickets.route.Route;
import com.example.tickets.route.RouteDto;

public interface AffilateLinkGenerator {
    String generate(Route route);

    String generate(RouteDto route);
}
