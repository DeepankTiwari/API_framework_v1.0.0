package com.Deepank.api.services;

import com.Deepank.api.client.ApiClient;
import com.Deepank.api.models.request.CreateBookingRequest;
import com.Deepank.api.models.request.UpdateBookingRequest;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingService {

    private static final String BOOKING_ENDPOINT = "/booking";
    private static final Logger LOG = LoggerFactory.getLogger(BookingService.class);

    private final ApiClient apiClient = new ApiClient();

public Response createBooking(CreateBookingRequest request) {
        LOG.info("Creating Booking");
        return apiClient.post(BOOKING_ENDPOINT, request);
    }

    public Response getBooking(int bookingId) {
        return apiClient.get(BOOKING_ENDPOINT + "/" + bookingId);
    }

    public Response updateBooking(int bookingid, UpdateBookingRequest request, String token) {
        return apiClient.put(BOOKING_ENDPOINT + "/" + bookingid, request, token);
    }

    public Response deleteBooking(int bookingid, String token) {
        return apiClient.delete(BOOKING_ENDPOINT + "/" + bookingid, token);
    }
}
