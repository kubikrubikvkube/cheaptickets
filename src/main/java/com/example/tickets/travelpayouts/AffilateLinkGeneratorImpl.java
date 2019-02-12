package com.example.tickets.travelpayouts;

import com.example.tickets.route.Route;
import com.example.tickets.route.RouteDto;
import com.example.tickets.route.RouteDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@PropertySource("classpath:ticket.properties")
public class AffilateLinkGeneratorImpl implements AffilateLinkGenerator {
    private static final Logger log = LoggerFactory.getLogger(AffilateLinkGeneratorImpl.class);
    private final RouteDtoMapper routeDtoMapper;
    private final String marker;


    public AffilateLinkGeneratorImpl(@Value("${travelpayouts.marker}") String marker, RouteDtoMapper routeDtoMapper) {
        log.debug("Marker: {}", marker);
        log.debug("RouteDtoMapper: {}", routeDtoMapper);

        this.routeDtoMapper = routeDtoMapper;
        this.marker = marker;
    }

    @Override
    public String generate(Route route) {
        log.debug("Generate: {}", route);
        RouteDto dto = routeDtoMapper.toDto(route);
        return generate(dto);
    }

    @Override
    public String generate(RouteDto route) {
        log.debug("Generate: {}", route);
        UriComponentsBuilder queryBuilder = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("www.aviasales.ru")
                .path("/search");

        queryBuilder.queryParam("origin_iata", route.getOrigin());
        queryBuilder.queryParam("destination_iata", route.getDestination());
        queryBuilder.queryParam("depart_date", route.getDepartTicket().getDepartDate());
        queryBuilder.queryParam("return_date", route.getReturnTicket().getDepartDate());
        queryBuilder.queryParam("with_request", true);
        queryBuilder.queryParam("marker", marker);

        return queryBuilder.build(true).toUriString();
    }
}
