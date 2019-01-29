package com.example.tickets.subscription;

public enum SubscriptionType {
    /**
     * Туда обратно в пункт Б не меньше чем на Х дней и не больше чем на Y дней
     */
    DESTINATION_TRIP_DURATION_FROM_TRIP_DURATION_TO,
    /**
     * Туда-обратно куда угодно не меньше чем на Х дней и не больше чем на Y дней
     */
    TRIP_DURATION_FROM_TRIP_DURATION_TO,
    /**
     * В одну сторону из пункта А в пункт Б
     */
    DESTINATION,
    /**
     * В одну сторону из пункта А куда угодно в назначенную дату
     */
    DESTINATION_DEPART_DATE,
    /**
     * Туда-обратно куда угодно в назначенные даты
     */
    DEPART_DATE_AND_RETURN_DATE,

    /**
     * Туда-обратно в пункт Б в указанные даты
     */
    DESTINATION_DEPART_DATE_RETURN_DATE,
    /**
     * Туда-обратно в пункт Б имея дату возврата, но не зная дату отправления
     */
    DESTINATION_RETURN_DATE,
    /**
     * Туда-обратно в пункт Б в определенную дату не меньше чем на Х дней
     */
    DESTINATION_DEPART_DATE_TRIP_DURATION_FROM,
    /**
     * Туда обратно в пункт Б не меньше чем на Х дней
     */
    DESTINATION_TRIP_DURATION_FROM,

    /**
     * Туда обратно в пункт Б не больше чем на Х дней
     */
    DESTINATION_TRIP_DURATION_TO,
    /**
     * Какие то параметры не валидны
     */
    INVALID

}
