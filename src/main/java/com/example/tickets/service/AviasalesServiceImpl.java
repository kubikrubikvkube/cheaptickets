package com.example.tickets.service;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.httpclient.DefaultHttpClient;
import com.example.tickets.service.response.AviasalesResponse;
import com.example.tickets.ticket.TicketJson;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Log
@Service
public class AviasalesServiceImpl implements AviasalesService {
    @Autowired
    private DefaultHttpClient<AviasalesResponse> httpClient;

    @Override
    public List<TicketJson> getOneWayTicket(String originIAT, String destinationIAT, LocalDate date, int range) throws ServiceException {
        StringBuilder sb = new StringBuilder();
        sb.append("https://lyssa.aviasales.ru/price_matrix?");
        sb.append("origin_iata=").append(originIAT).append("&");
        sb.append("destination_iata=").append(destinationIAT).append("&");
        sb.append("depart_start=").append(date).append("&");
        sb.append("depart_range=").append(range).append("&");
        sb.append("affiliate=false");
        var request = sb.toString();
        AviasalesResponse response = httpClient.getWithoutToken(request, AviasalesResponse.class);
        return response.getPrices();
    }

    @Override
    public List<TicketJson> getReturnTicket(String originIAT, String destinationIAT, LocalDate departure, LocalDate returnDate, int departRange, int returnRange) throws ServiceException {
        //https://lyssa.aviasales.ru/price_matrix?origin_iata=LED&destination_iata=MOW&depart_start=2018-12-21&return_start=2018-12-28&depart_range=6&return_range=6&affiliate=false
        StringBuilder sb = new StringBuilder();
        sb.append("https://lyssa.aviasales.ru/price_matrix?");
        sb.append("origin_iata=").append(originIAT).append("&");
        sb.append("destination_iata=").append(destinationIAT).append("&");
        sb.append("depart_start=").append(departure).append("&");
        sb.append("return_start=").append(returnDate).append("&");
        sb.append("depart_range=").append(departRange).append("&");
        sb.append("return_range=").append(returnRange).append("&");
        sb.append("affiliate=false");
        var request = sb.toString();
        AviasalesResponse response = httpClient.getWithoutToken(request, AviasalesResponse.class);
        return response.getPrices();
    }
}