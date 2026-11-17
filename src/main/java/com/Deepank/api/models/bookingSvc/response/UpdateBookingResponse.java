package com.Deepank.api.models.bookingSvc.response;

import com.Deepank.api.models.bookingSvc.BookingDates;
import com.Deepank.api.models.bookingSvc.BookingTestData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingResponse {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;

    public static UpdateBookingResponse createDefault(BookingTestData.BookingScenario data){
        return UpdateBookingResponse.builder()
                .firstname(data.getFirstname())
                .lastname(data.getLastname())
                .totalprice(data.getTotalprice())
                .depositpaid(data.getDepositpaid())
                .bookingdates(BookingDates.builder()
                        .checkin(data.getCheckin())
                        .checkout(data.getCheckout())
                        .build())
                .additionalneeds(data.getAdditionalneeds())
                .build();
    }
}
