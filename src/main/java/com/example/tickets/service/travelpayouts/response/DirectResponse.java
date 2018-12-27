package com.example.tickets.service.travelpayouts.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

/**
 * Билет без пересадок
 * <p>
 * Возвращает самый дешевый билет без пересадок для выбранного направления с фильтрами по датам вылета и возвращения.
 */

@Data
public class DirectResponse {
    private Boolean success;
    private List<JsonNode> data;
    private String error;

}
