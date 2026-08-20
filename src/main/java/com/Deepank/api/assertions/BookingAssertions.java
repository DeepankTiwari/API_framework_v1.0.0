package com.Deepank.api.assertions;

import com.Deepank.api.models.request.CreateBookingRequest;
import com.Deepank.api.models.request.UpdateBookingRequest;
import com.Deepank.api.models.response.GetBookingResponse;
import org.testng.Assert;


public class BookingAssertions {

    public static void assertBookingMatches(GetBookingResponse actual, CreateBookingRequest expected) {

        Assert.assertEquals(actual.getFirstname(), expected.getFirstname());
        Assert.assertEquals(actual.getLastname(), expected.getLastname());
        Assert.assertEquals(actual.getTotalprice(), expected.getTotalprice());
        Assert.assertEquals(actual.isDepositpaid(), expected.isDepositpaid());
        Assert.assertEquals(actual.getBookingdates().getCheckin(), expected.getBookingdates().getCheckin());
        Assert.assertEquals(actual.getBookingdates().getCheckout(), expected.getBookingdates().getCheckout());
        Assert.assertEquals(actual.getAdditionalneeds(), expected.getAdditionalneeds());
    }

    public static void assertBookingMatches(GetBookingResponse actual, UpdateBookingRequest expected) {

        Assert.assertEquals(actual.getFirstname(), expected.getFirstname());
        Assert.assertEquals(actual.getLastname(), expected.getLastname());
        Assert.assertEquals(actual.getTotalprice(), expected.getTotalprice());
        Assert.assertEquals(actual.isDepositpaid(), expected.isDepositpaid());
        Assert.assertEquals(actual.getBookingdates().getCheckin(), expected.getBookingdates().getCheckin());
        Assert.assertEquals(actual.getBookingdates().getCheckout(), expected.getBookingdates().getCheckout());
        Assert.assertEquals(actual.getAdditionalneeds(), expected.getAdditionalneeds());
    }
}
