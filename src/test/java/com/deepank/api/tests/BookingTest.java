package com.deepank.api.tests;


import com.Deepank.api.assertions.DTOComparisonUtil;
import com.Deepank.api.controllers.BookingControllers;
import com.Deepank.api.models.apiClient.response.ApiResponse;
import com.Deepank.api.models.bookingSvc.request.CreateBookingRequest;
import com.Deepank.api.models.bookingSvc.request.UpdateBookingRequest;
import com.Deepank.api.models.bookingSvc.response.CreateBookingResponse;
import com.Deepank.api.models.bookingSvc.response.GetBookingResponse;
import com.Deepank.api.models.bookingSvc.response.UpdateBookingResponse;
import com.Deepank.api.models.bookingSvc.BookingTestData;
import com.deepank.api.testdata.BookingDataProvider;
import com.deepank.api.retry.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;


public class BookingTest extends BaseTest {

    private final BookingControllers bookingControllers = new BookingControllers();


    @Test(
            groups = {"smoke", "booking"},
            dataProvider = "createBookingData",
            dataProviderClass = BookingDataProvider.class,
            retryAnalyzer = RetryAnalyzer.class
    )
    public void createAndGetBookingTest(BookingTestData.BookingScenario testData) {

        // create booking
        CreateBookingRequest createBookingRequest = CreateBookingRequest.createBookingRequest(testData);

        ApiResponse createBookingActualResponseJson = bookingControllers.createBooking(createBookingRequest, 200);
        CreateBookingResponse createBookingActualResponseDto = createBookingActualResponseJson.as(CreateBookingResponse.class);
        int bookingId = createBookingActualResponseDto.getBookingid();
        CreateBookingResponse createBookingExpectedResponse = CreateBookingResponse.createDefault(createBookingRequest);
        createBookingExpectedResponse.setBookingid(bookingId);


        DTOComparisonUtil.compareAndThrow(createBookingExpectedResponse, createBookingActualResponseJson.asString());


        // Get booking after Create Booking
        ApiResponse getBookingResponse = bookingControllers.getBooking(bookingId, 200);
        GetBookingResponse expectedGetBookingResponseDTO = GetBookingResponse.createDefault(testData);

        DTOComparisonUtil.compareAndThrow(expectedGetBookingResponseDTO, getBookingResponse.asString());

        // Update booking
        BookingTestData.BookingScenario updateTestData = testData.toBuilder()
                .firstname("Shashank")
                .lastname("Pandey")
                .checkin("2026-08-22")
                .checkout("2026-08-27")
                .additionalneeds("Breakfast")
                .build();

        UpdateBookingRequest updateBookingRequest = UpdateBookingRequest.createUpdateRequest(updateTestData);
        ApiResponse updateBookingResponse = bookingControllers.updateBookingComplete(updateBookingRequest, bookingId, 200);
        UpdateBookingResponse expectedUpdateBookingResponse = UpdateBookingResponse.createDefault(updateTestData);

        DTOComparisonUtil.compareAndThrow(expectedUpdateBookingResponse, updateBookingResponse.asString());

        // Get booking after Update
        ApiResponse getBookingResponseAfterUpdate = bookingControllers.getBooking(bookingId, 200);
        GetBookingResponse expectedGetBookingResponseDTOAfterUpdate = GetBookingResponse.createDefault(updateTestData);
        DTOComparisonUtil.compareAndThrow(expectedGetBookingResponseDTOAfterUpdate, getBookingResponseAfterUpdate.asString());


        // Delete booking
        ApiResponse deleteBookingResponse = bookingControllers.deleteBooking(bookingId, 201);
        Assert.assertEquals(deleteBookingResponse.asString(), "Created", "Delete booking response body mismatch");
//        Response deleteBookingResponse = bookingService.deleteBooking(bookingId, token);
//        Assert.assertEquals(deleteBookingResponse.statusCode(), 201);


        // Get booking after delete
        ApiResponse getBookingResponseAfterDelete = bookingControllers.getBooking(bookingId,404);
        Assert.assertEquals(getBookingResponseAfterDelete.asString(), "Not Found", "After delete booking Get booking response body mismatch");
//        Response getBookingAfterDeleteResponse = bookingService.getBooking(bookingId);
//        Assert.assertEquals(getBookingAfterDeleteResponse.statusCode(), 404);
    }


    @Test(
            dataProviderClass = BookingDataProvider.class,
            dataProvider = "invalidAuthData",
            retryAnalyzer = RetryAnalyzer.class
    )
    public void updateBookingInvalidAuthValidations(
            String scenariosName,
            String token,
            int expectedStatusCode
    ){
        LOG.info("Running scenarios -> {}", scenariosName);

        UpdateBookingRequest request = UpdateBookingRequest.createUpdateRequest(BookingDataProvider.getBaseCreateScenario());
        ApiResponse updateBooking = bookingControllers.updateBookingComplete(request, 1234, token, expectedStatusCode);
        Assert.assertEquals(updateBooking.asString(), "Forbidden");

    }
}
