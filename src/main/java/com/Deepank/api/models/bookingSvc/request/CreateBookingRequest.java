package com.Deepank.api.models.bookingSvc.request;

import com.Deepank.api.models.bookingSvc.BookingDates;

import com.Deepank.api.models.bookingSvc.BookingTestData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;

    public static CreateBookingRequest createBookingRequest(BookingTestData.BookingScenario data){
        return CreateBookingRequest.builder()
                .firstname(data.getFirstname())
                .lastname(data.getLastname())
                .totalprice(data.getTotalprice())
                .depositpaid(data.getDepositpaid() != null ? data.getDepositpaid() : false)
                .bookingdates(BookingDates.builder()
                        .checkin(data.getCheckin())
                        .checkout(data.getCheckout())
                        .build())
                .additionalneeds(data.getAdditionalneeds())
                .build();
    }
}
