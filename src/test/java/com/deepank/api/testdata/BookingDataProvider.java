package com.deepank.api.testdata;

import com.Deepank.api.models.bookingSvc.BookingDates;
import com.Deepank.api.models.bookingSvc.BookingTestData;
import org.testng.annotations.DataProvider;

public class BookingDataProvider {

    private static BookingTestData testData;

    private static BookingTestData getTestData(){
        if(testData == null){
            testData = JsonDataReader.loadTestData(
                    "/testdata/booking_scenarios.json",
                    BookingTestData.class
            );
        }
        return testData;
    }

    public static BookingTestData.BookingScenario getBaseCreateScenario() {

        return getTestData()
                .getCreateScenarios()
                .getFirst();
    }

    @DataProvider(name = "createBookingData")
    public static Object[][] createBookingData() {
        return getTestData().getCreateScenarios()
                .stream()
                .map(data -> new Object[]{data})
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "invalidAuthData")
    public static Object[][] invalidAuthData() {
        return new Object[][]{
                {"missing token", null, 403},
                {"invalid token", "invalid_token", 403}
        };
    }

    @DataProvider(name = "invalidBookingData")
    public static Object[][] invalidBookingData() {
        BookingTestData.BookingScenario baseTestData = getTestData().getCreateScenarios().getFirst();
        return new Object[][]{
                {
                    "empty firstname",
                        baseTestData.toBuilder()
                            .firstname("")
                            .build(),
                        400
                },
                {
                    "negative price",
                        baseTestData.toBuilder()
                                .totalprice(-1)
                                .build(),
                        400
                },
                {
                    "invalid date range",
                        baseTestData.toBuilder()
                                .checkin("2026-08-20")
                                .checkout("2026-08-15")
                                .build(),
                        400
                }
        };
    }
}
