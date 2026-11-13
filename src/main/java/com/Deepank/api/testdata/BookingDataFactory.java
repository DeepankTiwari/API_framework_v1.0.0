package com.Deepank.api.testdata;

import com.Deepank.api.models.bookingSvc.BookingDates;
import com.Deepank.api.models.bookingSvc.request.CreateBookingRequest;
import com.Deepank.api.models.bookingSvc.request.UpdateBookingRequest;

public final class BookingDataFactory {

    private BookingDataFactory() {

    }

    public static CreateBookingRequest validCreateBookingWithDepositPaid () {
        return CreateBookingRequest.builder()
                .firstname("Deepank")
                .lastname("Tiwari")
                .totalprice(500)
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin("2026-08-20")
                        .checkout("2026-08-25")
                        .build())
                .additionalneeds("Breakfast")
                .build();
    }

    public static CreateBookingRequest validCreateBookingWithDepositUnPaid () {
        return CreateBookingRequest.builder()
                .firstname("Sagar")
                .lastname("Tiwari")
                .totalprice(500)
                .depositpaid(false)
                .bookingdates(BookingDates.builder()
                        .checkin("2026-08-20")
                        .checkout("2026-08-25")
                        .build())
                .additionalneeds("WiFI")
                .build();
    }

    public static UpdateBookingRequest validUpdateBooking() {
        return UpdateBookingRequest.builder()
                .firstname("Deepank")
                .lastname("Tiwari")
                .totalprice(500)
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin("2026-08-22")
                        .checkout("2026-08-27")
                        .build())
                .additionalneeds("Dinner")
                .build();
    }
}
