package com.Deepank.api.models.response;

import com.Deepank.api.models.request.CreateBookingRequest;
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
}
