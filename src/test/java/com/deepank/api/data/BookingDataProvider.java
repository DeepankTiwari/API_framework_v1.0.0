package com.deepank.api.data;

import com.Deepank.api.testdata.BookingDataFactory;
import org.testng.annotations.DataProvider;

public class BookingDataProvider {


    @DataProvider(name = "bookingData")
    public static Object[][] bookingData() {
        return new Object[][]{
                {BookingDataFactory.validCreateBookingWithDepositPaid()},
                {BookingDataFactory.validCreateBookingWithDepositUnPaid()}
        };
    }
}
