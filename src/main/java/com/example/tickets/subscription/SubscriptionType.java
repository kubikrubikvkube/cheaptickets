package com.example.tickets.subscription;

public enum SubscriptionType {
    /**
     * Туда обратно в пункт Б не меньше чем на Х дней и не больше чем на Y дней
     */
    RETURN_DESTINATION_TRIP_DURATION_FROM_TRIP_DURATION_TO,
    /**
     * Туда-обратно куда угодно не меньше чем на Х дней и не больше чем на Y дней
     */
    RETURN_TRIP_DURATION_FROM_TRIP_DURATION_TO,
    /**
     * В одну сторону из пункта А в пункт Б
     */
    ONE_WAY_DESTINATION,
    /**
     * В одну сторону из пункта А куда угодно в назначенную дату
     */
    ONE_WAY_DESTINATION_AND_DEPART_DATE,
    /**
     * Туда-обратно куда угодно в назначенные даты
     */
    RETURN_NO_DESTINATION_DEPART_DATE_AND_RETURN_DATE,

    /**
     * Туда-обратно в пункт Б имея дату возврата, но не зная дату отправления
     */
    RETURN_DESTINATION_RETURN_DATE,
    /**
     * Туда-обратно в пункт Б в определенную дату не меньше чем на Х дней
     */
    RETURN_DESTINATION_DEPART_DATE_TRIP_DURATION_FROM,
    /**
     * Туда обратно в пункт Б не меньше чем на Х дней
     */
    RETURN_DESTINATION_TRIP_DURATION_FROM,

    /**
     * Туда обратно в пункт Б не больше чем на Х дней
     */
    RETURN_DESTINATION_TRIP_DURATION_TO,
    /**
     * Какие то параметры не валидны
     */
    INVALID

}
