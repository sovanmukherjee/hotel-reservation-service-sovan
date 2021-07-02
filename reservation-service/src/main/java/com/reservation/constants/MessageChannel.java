package com.reservation.constants;

public class MessageChannel {
private MessageChannel() {}
public static final String RESERVATION_CREATE_REQUEST_CH = "reservationCreateRequest-out";
public static final String PAYMENT_CREATED_EVENT_CH = "paymentCreatedEvent-out";
public static final String PAYMENT_FAILED_EVENT_CH = "paymentFailedEvent-out";
}
