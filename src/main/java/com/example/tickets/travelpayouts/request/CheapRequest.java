package com.example.tickets.travelpayouts.request;

import lombok.Builder;
import lombok.Value;

/**
 * Самые дешевые авиабилеты
 * <p>
 * Возвращает самые дешевые билеты без пересадок, а так же с 1 и 2 пересадками для выбранного направления с фильтрами по датам вылета и возвращения.
 * Билеты возвращаются на рейсы туда-обратно.
 */
@Value
@Builder
public class CheapRequest {
}
