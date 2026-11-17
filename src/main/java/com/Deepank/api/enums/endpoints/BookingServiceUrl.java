package com.Deepank.api.enums.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingServiceUrl {

    BOOKING("/booking"),
    BOOKING_BY_ID("/booking/{bookingId}");

    public final String path;
}


