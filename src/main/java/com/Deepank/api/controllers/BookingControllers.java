package com.Deepank.api.controllers;

import com.Deepank.api.auth.TokenManager;
import com.Deepank.api.client.RestApiClient;
import com.Deepank.api.enums.endpoints.BookingServiceUrl;
import com.Deepank.api.enums.httpMethods.HttpMethod;
import com.Deepank.api.models.apiClient.request.ApiRequest;
import com.Deepank.api.models.apiClient.response.ApiResponse;
import com.Deepank.api.models.bookingSvc.request.CreateBookingRequest;
import com.Deepank.api.models.bookingSvc.request.UpdateBookingRequest;
import com.Deepank.api.models.bookingSvc.response.UpdateBookingResponse;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Map;

public class BookingControllers extends RestApiClient {

    private final AuthControllers authController = new AuthControllers();
    private final TokenManager tokenManager = new TokenManager(authController);

    public ApiResponse createBooking(CreateBookingRequest request, int expectedStatusCode) {
        String endpoint = BookingServiceUrl.BOOKING.getPath();
        ApiRequest apiRequest = ApiRequest.builder()
                .endpoint(endpoint)
                .httpMethod(HttpMethod.POST)
                .body(request)
                .build();

        Response response = processRequest(apiRequest);
        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Response StatusCode mismatch with response - > " + response.getBody().asString());

        return new ApiResponse(response);
    }


    public ApiResponse getBooking(int bookingId, int expectedStatusCode){
        Map<String, ?> pathParams = Map.of(
                "bookingId", bookingId
        );
        String endpoint = BookingServiceUrl.BOOKING_BY_ID.getPath();
        ApiRequest apiRequest = ApiRequest.builder()
                .endpoint(endpoint)
                .httpMethod(HttpMethod.GET)
                .pathParams(pathParams)
                .build();

        Response response = processRequest(apiRequest);
        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Response statusCode mismatch");

        return new ApiResponse(response);
    }


    public ApiResponse updateBookingComplete(UpdateBookingRequest request, int bookingId, String token, int expectedStatusCode){
        Map<String, ?> pathParams = Map.of(
                "bookingId", bookingId
        );
        Map<String, ?> headers = Map.of(
                "Cookie", "token=" + token
        );
        String endpoint = BookingServiceUrl.BOOKING_BY_ID.getPath();
        ApiRequest apiRequest = ApiRequest.builder()
                .endpoint(endpoint)
                .httpMethod(HttpMethod.PUT)
                .pathParams(pathParams)
                .headers(headers)
                .body(request)
                .build();

        Response response = processRequest(apiRequest);
        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Response statusCode mismatch");

        return new ApiResponse(response);
    }

    public ApiResponse updateBookingComplete(UpdateBookingRequest request, int bookingId, int expectedStatusCode){
        return updateBookingComplete(request, bookingId, tokenManager.getToken(), expectedStatusCode);
    }

    public ApiResponse updateBookingPartial(UpdateBookingRequest request, int bookingId, String token, int expectedStatusCode){
        Map<String, ?> pathParams = Map.of(
                "bookingId", bookingId
        );
        Map<String, ?> headers = Map.of(
                "Cookie", "token=" + token
        );
        String endpoint = BookingServiceUrl.BOOKING_BY_ID.getPath();
        ApiRequest apiRequest = ApiRequest.builder()
                .endpoint(endpoint)
                .httpMethod(HttpMethod.PUT)
                .pathParams(pathParams)
                .headers(headers)
                .body(request)
                .build();

        Response response = processRequest(apiRequest);
        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Response statusCode mismatch");

        return new ApiResponse(response);
    }


    public ApiResponse deleteBooking(int bookingId, String token, int expectedStatusCode){
        String endpoint = BookingServiceUrl.BOOKING_BY_ID.getPath();
        Map<String, ?> pathParams = Map.of(
                "bookingId", bookingId
        );
        Map<String, ?> headers = Map.of(
                "Cookie", "token=" + token
        );
        ApiRequest apiRequest = ApiRequest.builder()
                .endpoint(endpoint)
                .httpMethod(HttpMethod.DELETE)
                .headers(headers)
                .pathParams(pathParams)
                .build();
        Response response = processRequest(apiRequest);
        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Response statusCode mismatch");

        return new ApiResponse(response);
    }

    public ApiResponse deleteBooking(int bookingId, int expectedStatusCode){
        return deleteBooking(bookingId, tokenManager.getToken(), expectedStatusCode);
    }
}
