package com.deepank.api.tests;


import com.Deepank.api.assertions.BookingAssertions;
import com.Deepank.api.auth.TokenManager;
import com.Deepank.api.models.request.CreateBookingRequest;
import com.Deepank.api.models.request.UpdateBookingRequest;
import com.Deepank.api.models.response.CreateBookingResponse;
import com.Deepank.api.models.response.GetBookingResponse;
import com.Deepank.api.reporting.AllureAttachentUtil;
import com.Deepank.api.services.BookingService;
import com.Deepank.api.testdata.BookingDataFactory;
import com.deepank.api.data.BookingDataProvider;
import com.deepank.api.retry.RetryAnalyzer;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class BookingTest extends BaseTest {

    private final BookingService bookingService = new BookingService();

    @Test(
            groups = {"smoke", "booking"},
            dataProvider = "bookingData",
            dataProviderClass = BookingDataProvider.class,
            retryAnalyzer = RetryAnalyzer.class
    )
    public void createAndGetBookingTest(CreateBookingRequest createBookingRequest) {

//        CreateBookingRequest createBookingRequest = request;

        Response createBookingResponse = bookingService.createBooking(createBookingRequest);
        CreateBookingResponse createBookingResponseDTO = createBookingResponse.as(CreateBookingResponse.class);

        AllureAttachentUtil.attachResponse(createBookingResponse.asPrettyString());

        Assert.assertEquals(createBookingResponse.statusCode(), 200);
//        Assert.assertNotEquals(createBookingResponseDTO.getBooking().getBookingdates().getCheckin(), "2026-08-22");
//        Assert.assertEquals(createBookingResponseDTO.getBooking().getBookingdates().getCheckin(), "2026-08-20");
//        Assert.assertNotEquals(createBookingResponseDTO.getBooking().getBookingdates().getCheckout(), "2026-08-27");
//        Assert.assertEquals(createBookingResponseDTO.getBooking().getBookingdates().getCheckout(), "2026-08-25");
//        Assert.assertNotEquals(createBookingResponseDTO.getBooking().getAdditionalneeds(), "Dinner");
//        Assert.assertEquals(createBookingResponseDTO.getBooking().getAdditionalneeds(), "Breakfast");

        int bookingId = createBookingResponseDTO.getBookingid();
        Assert.assertTrue(bookingId > 0, "BookingId is invalid");

        // Get call for same bookingID
        Response getBookingResponse = bookingService.getBooking(bookingId);
        getBookingResponse.then().body(matchesJsonSchemaInClasspath("schemas/get-booking-schema.json"));
        GetBookingResponse getBookingResponseDTO = getBookingResponse.as(GetBookingResponse.class);

        Assert.assertEquals(getBookingResponse.statusCode(), 200);
        BookingAssertions.assertBookingMatches(getBookingResponseDTO, createBookingRequest);

        // Auth token
        String token = new TokenManager().getToken();

        // Update booking
        UpdateBookingRequest updateBookingRequest = BookingDataFactory.validUpdateBooking();

        Response updateBookingResponse = bookingService.updateBooking(bookingId, updateBookingRequest, token);
        Assert.assertEquals(updateBookingResponse.statusCode(), 200);
//        Assert.assertEquals(updateBookingResponse.jsonPath().getString("bookingdates.checkin"), "2026-08-22");
//        Assert.assertNotEquals(updateBookingResponse.jsonPath().getString("bookingdates.checkin"), "2026-08-20");
//        Assert.assertEquals(updateBookingResponse.jsonPath().getString("bookingdates.checkout"), "2026-08-27");
//        Assert.assertNotEquals(updateBookingResponse.jsonPath().getString("bookingdates.checkout"), "2026-08-25");
//        Assert.assertEquals(updateBookingResponse.jsonPath().getString("additionalneeds"), "Dinner");
//        Assert.assertNotEquals(updateBookingResponse.jsonPath().getString("additionalneeds"), "Breakfast");

        // Get Booking after Update
        Response getBookingAfterUpdate = bookingService.getBooking(bookingId);
        GetBookingResponse getBookingAfterUpdateResponseDTO = getBookingAfterUpdate.as(GetBookingResponse.class);
        BookingAssertions.assertBookingMatches(getBookingAfterUpdateResponseDTO, updateBookingRequest);


        // Delete booking
        Response deleteBookingResponse = bookingService.deleteBooking(bookingId, token);
        Assert.assertEquals(deleteBookingResponse.statusCode(), 201);

        // Get booking after delete
        Response getBookingAfterDeleteResponse = bookingService.getBooking(bookingId);
        Assert.assertEquals(getBookingAfterDeleteResponse.statusCode(), 404);
    }


}
