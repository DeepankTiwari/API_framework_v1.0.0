package com.Deepank.api.models.bookingSvc.response;

import com.Deepank.api.models.bookingSvc.request.CreateBookingRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingResponse {

    private int bookingid;
    private CreateBookingRequest booking;

    public static CreateBookingResponse createDefault(CreateBookingRequest createBookingRequest){
        return CreateBookingResponse.builder()
                .bookingid(1234)
                .booking(createBookingRequest)
                .build();
    }
}
