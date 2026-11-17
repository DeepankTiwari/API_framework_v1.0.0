package com.deepank.api.assertions;


import com.Deepank.api.assertions.ComparisonOptions;
import com.Deepank.api.assertions.ComparisonResult;
import com.Deepank.api.assertions.DTOComparisonUtil;
import com.Deepank.api.assertions.ComparisonDifference;
import com.Deepank.api.assertions.DifferenceType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class DTOComparisonUtilTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /*
     * =========================================================
     * BASELINE ACTUAL JSON
     * =========================================================
     *
     * Every negative test starts from this valid response and
     * changes ONLY the thing that test wants to verify.
     *
     * This prevents unrelated differences from polluting tests.
     */

    private static final String MATCHING_ACTUAL_JSON = """
            {
              "bookingid": 1234,
              "booking": {
                "firstname": "Deepank",
                "lastname": "Tiwari",
                "totalprice": 500,
                "depositpaid": true,
                "comment": null,
                "bookingdates": {
                  "checkin": "2026-09-23",
                  "checkout": "2026-09-25"
                },
                "items": [
                  {
                    "id": 1,
                    "name": "A"
                  },
                  {
                    "id": 2,
                    "name": "B"
                  }
                ]
              }
            }
            """;


    /*
     * =========================================================
     * TEST DTOs
     * =========================================================
     */

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class BookingResponse {

        private Integer bookingid;

        private Booking booking;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Booking {

        private String firstname;

        private String lastname;

        private Integer totalprice;

        private Boolean depositpaid;

        private String comment;

        private BookingDates bookingdates;

        private List<Item> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class BookingDates {

        private String checkin;

        private String checkout;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Item {

        private Integer id;

        private String name;
    }


    /*
     * =========================================================
     * 1. EXACT MATCH
     * =========================================================
     */

    @Test
    public void shouldPassWhenExpectedAndActualAreSame() {

        DTOComparisonUtil.compareAndThrow(
                createExpectedResponse(),
                MATCHING_ACTUAL_JSON
        );
    }


    /*
     * =========================================================
     * 2. VALUE MISMATCH
     * =========================================================
     */

    @Test
    public void shouldDetectValueMismatch() {

        ObjectNode actual = createMatchingActualNode();

        actual.put(
                "bookingid",
                9999
        );

        AssertionError error =
                expectComparisonFailure(actual);

        assertErrorContains(
                error,
                "bookingid",
                "VALUE_MISMATCH"
        );
    }


    /*
     * =========================================================
     * 3. NESTED VALUE MISMATCH
     * =========================================================
     */

    @Test
    public void shouldDetectNestedValueMismatch() {

        ObjectNode actual = createMatchingActualNode();

        getBooking(actual)
                .put(
                        "firstname",
                        "WrongName"
                );

        AssertionError error =
                expectComparisonFailure(actual);

        assertErrorContains(
                error,
                "booking.firstname",
                "VALUE_MISMATCH"
        );
    }


    /*
     * =========================================================
     * 4. TYPE MISMATCH
     *
     * Expected:
     *      500
     *
     * Actual:
     *      "500"
     *
     * This is the MAIN reason we compare against raw JSON.
     * =========================================================
     */

    @Test
    public void shouldDetectStringVsNumberTypeMismatch() {

        ObjectNode actual = createMatchingActualNode();

        getBooking(actual)
                .put(
                        "totalprice",
                        "500"
                );

        AssertionError error =
                expectComparisonFailure(actual);

        assertErrorContains(
                error,
                "booking.totalprice",
                "TYPE_MISMATCH"
        );
    }


    /*
     * =========================================================
     * 5. TYPE MISMATCH MUST NOT ALSO CREATE VALUE MISMATCH
     * =========================================================
     */

    @Test
    public void shouldNotReportValueMismatchAfterTypeMismatch() {

        ObjectNode actual = createMatchingActualNode();

        getBooking(actual)
                .put(
                        "totalprice",
                        "500"
                );

        ComparisonResult result =
                DTOComparisonUtil.compare(
                        createExpectedResponse(),
                        actual.toString(),
                        ComparisonOptions.defaultOptions()
                );

        Assert.assertFalse(
                result.isMatch()
        );

        Assert.assertEquals(
                result.getDifferences().size(),
                1,
                "Only one difference should be produced"
        );

        ComparisonDifference difference =
                result.getDifferences().get(0);

        Assert.assertEquals(
                difference.getPath(),
                "booking.totalprice"
        );

        Assert.assertEquals(
                difference.getDifferenceType(),
                DifferenceType.TYPE_MISMATCH
        );
    }


    /*
     * =========================================================
     * 6. MISSING FIELD
     * =========================================================
     */

    @Test
    public void shouldDetectMissingField() {

        ObjectNode actual = createMatchingActualNode();

        getBooking(actual)
                .remove("lastname");

        AssertionError error =
                expectComparisonFailure(actual);

        assertErrorContains(
                error,
                "booking.lastname",
                "MISSING_FIELD"
        );
    }


    /*
     * =========================================================
     * 7. EXTRA FIELD
     * =========================================================
     */

    @Test
    public void shouldDetectExtraField() {

        ObjectNode actual = createMatchingActualNode();

        getBooking(actual)
                .put(
                        "hello",
                        "unexpected"
                );

        AssertionError error =
                expectComparisonFailure(actual);

        assertErrorContains(
                error,
                "booking.hello",
                "EXTRA_FIELD"
        );
    }


    /*
     * =========================================================
     * 8. NULL VS NON-NULL
     * =========================================================
     */

    @Test
    public void shouldDetectNullVsNonNull() {

        ObjectNode actual = createMatchingActualNode();

        getBooking(actual)
                .putNull("firstname");

        AssertionError error =
                expectComparisonFailure(actual);

        assertErrorContains(
                error,
                "booking.firstname",
                "NULL_VS_NON_NULL"
        );
    }


    /*
     * =========================================================
     * 9. EXPLICIT NULL SHOULD MATCH EXPLICIT NULL
     *
     * Expected DTO:
     *      comment = null
     *
     * Actual JSON:
     *      "comment": null
     * =========================================================
     */

    @Test
    public void shouldPassWhenBothExpectedAndActualAreExplicitNull() {

        DTOComparisonUtil.compareAndThrow(
                createExpectedResponse(),
                MATCHING_ACTUAL_JSON
        );
    }


    /*
     * =========================================================
     * 10. NULL FIELD != MISSING FIELD
     *
     * Expected:
     *      "comment": null
     *
     * Actual:
     *      comment doesn't exist
     * =========================================================
     */

    @Test
    public void shouldDetectDifferenceBetweenNullAndMissingField() {

        ObjectNode actual = createMatchingActualNode();

        getBooking(actual)
                .remove("comment");

        AssertionError error =
                expectComparisonFailure(actual);

        assertErrorContains(
                error,
                "booking.comment",
                "MISSING_FIELD"
        );
    }


    /*
     * =========================================================
     * 11. ARRAY EXACT MATCH
     * =========================================================
     */

    @Test
    public void shouldPassWhenArrayMatchesExactly() {

        DTOComparisonUtil.compareAndThrow(
                createExpectedResponse(),
                MATCHING_ACTUAL_JSON
        );
    }


    /*
     * =========================================================
     * 12. ARRAY ELEMENT VALUE MISMATCH
     *
     * Expected:
     *      items[1].name = B
     *
     * Actual:
     *      items[1].name = WRONG
     * =========================================================
     */

    @Test
    public void shouldDetectArrayElementValueMismatch() {

        ObjectNode actual = createMatchingActualNode();

        ArrayNode items =
                getItems(actual);

        ObjectNode secondItem =
                (ObjectNode) items.get(1);

        secondItem.put(
                "name",
                "WRONG"
        );

        AssertionError error =
                expectComparisonFailure(actual);

        assertErrorContains(
                error,
                "booking.items[1].name",
                "VALUE_MISMATCH"
        );
    }


    /*
     * =========================================================
     * 13. ARRAY SIZE MISMATCH
     * =========================================================
     */

    @Test
    public void shouldDetectArraySizeMismatch() {

        ObjectNode actual = createMatchingActualNode();

        ArrayNode items =
                getItems(actual);

        items.remove(1);

        AssertionError error =
                expectComparisonFailure(actual);

        assertErrorContains(
                error,
                "booking.items",
                "ARRAY_SIZE_MISMATCH"
        );
    }


    /*
     * =========================================================
     * 14. NESTED OBJECT INSIDE ARRAY MISMATCH
     * =========================================================
     */

    @Test
    public void shouldDetectNestedObjectInsideArrayMismatch() {

        ObjectNode actual = createMatchingActualNode();

        ArrayNode items =
                getItems(actual);

        ObjectNode firstItem =
                (ObjectNode) items.get(0);

        firstItem.put(
                "id",
                999
        );

        AssertionError error =
                expectComparisonFailure(actual);

        assertErrorContains(
                error,
                "booking.items[0].id",
                "VALUE_MISMATCH"
        );
    }


    /*
     * =========================================================
     * 15. ARRAY ORDER SHOULD MATTER
     *
     * Current V1 comparator is order-sensitive.
     * =========================================================
     */

    @Test
    public void shouldDetectArrayOrderMismatch() {

        ObjectNode actual =
                createMatchingActualNode();

        ArrayNode items =
                getItems(actual);

        JsonNode first =
                items.get(0).deepCopy();

        JsonNode second =
                items.get(1).deepCopy();

        items.set(
                0,
                second
        );

        items.set(
                1,
                first
        );

        AssertionError error =
                expectComparisonFailure(actual);

        assertErrorContains(
                error,
                "booking.items[0]"
        );
    }


    /*
     * =========================================================
     * 16. IGNORE EXACT FIELD PATH
     * =========================================================
     */

    @Test
    public void shouldIgnoreExactFieldPath() {

        ObjectNode actual =
                createMatchingActualNode();

        getBooking(actual)
                .put(
                        "totalprice",
                        999999
                );

        ComparisonOptions options =
                ComparisonOptions.builder()
                        .ignoreField(
                                "booking.totalprice"
                        )
                        .build();

        DTOComparisonUtil.compareAndThrow(
                createExpectedResponse(),
                actual.toString(),
                options
        );
    }


    /*
     * =========================================================
     * 17. IGNORE MISSING FIELD
     *
     * If a path is explicitly ignored, it should not matter
     * whether that field is missing.
     * =========================================================
     */

    @Test
    public void shouldIgnoreMissingFieldWhenPathIsIgnored() {

        ObjectNode actual =
                createMatchingActualNode();

        getBooking(actual)
                .remove("comment");

        ComparisonOptions options =
                ComparisonOptions.builder()
                        .ignoreField(
                                "booking.comment"
                        )
                        .build();

        DTOComparisonUtil.compareAndThrow(
                createExpectedResponse(),
                actual.toString(),
                options
        );
    }


    /*
     * =========================================================
     * 18. IGNORE EXTRA FIELD
     * =========================================================
     */

    @Test
    public void shouldIgnoreExtraFieldWhenPathIsIgnored() {

        ObjectNode actual =
                createMatchingActualNode();

        getBooking(actual)
                .put(
                        "hello",
                        "something"
                );

        ComparisonOptions options =
                ComparisonOptions.builder()
                        .ignoreField(
                                "booking.hello"
                        )
                        .build();

        DTOComparisonUtil.compareAndThrow(
                createExpectedResponse(),
                actual.toString(),
                options
        );
    }


    /*
     * =========================================================
     * 19. MULTIPLE DIFFERENCES SHOULD ALL BE COLLECTED
     * =========================================================
     */

    @Test
    public void shouldCollectMultipleDifferences() {

        ObjectNode actual =
                createMatchingActualNode();

        /*
         * Difference 1
         */
        actual.put(
                "bookingid",
                9999
        );


        ObjectNode booking =
                getBooking(actual);

        /*
         * Difference 2
         */
        booking.put(
                "firstname",
                "Wrong"
        );

        /*
         * Difference 3
         */
        booking.remove(
                "lastname"
        );

        /*
         * Difference 4
         *
         * Type mismatch only.
         * Must NOT additionally produce VALUE_MISMATCH.
         */
        booking.put(
                "totalprice",
                "500"
        );

        /*
         * Difference 5
         */
        booking.put(
                "depositpaid",
                false
        );


        ObjectNode bookingDates =
                (ObjectNode)
                        booking.get(
                                "bookingdates"
                        );

        /*
         * Difference 6
         */
        bookingDates.put(
                "checkin",
                "2030-01-01"
        );


        ArrayNode items =
                (ArrayNode)
                        booking.get(
                                "items"
                        );

        /*
         * Difference 7
         */
        ((ObjectNode) items.get(1))
                .put(
                        "name",
                        "WRONG"
                );

        /*
         * Difference 8
         */
        booking.put(
                "unexpected",
                "field"
        );


        ComparisonResult result =
                DTOComparisonUtil.compare(
                        createExpectedResponse(),
                        actual.toString(),
                        ComparisonOptions.defaultOptions()
                );


        Assert.assertFalse(
                result.isMatch()
        );

        Assert.assertEquals(
                result.getDifferences().size(),
                8,
                "Comparator should collect all differences"
        );


        AssertionError error =
                expectComparisonFailure(actual);


        assertErrorContains(
                error,
                "bookingid",
                "booking.firstname",
                "booking.lastname",
                "booking.totalprice",
                "booking.depositpaid",
                "booking.bookingdates.checkin",
                "booking.items[1].name",
                "booking.unexpected"
        );
    }


    /*
     * =========================================================
     * 20. MALFORMED ACTUAL JSON
     * =========================================================
     */

    @Test
    public void shouldThrowForMalformedActualJson() {

        String malformedJson = """
                {
                  "bookingid": 1234,
                  "booking":
                """;

        IllegalArgumentException error =
                Assert.expectThrows(
                        IllegalArgumentException.class,
                        () ->
                                DTOComparisonUtil.compareAndThrow(
                                        createExpectedResponse(),
                                        malformedJson
                                )
                );

        /*
         * Don't make this test depend heavily on exact wording.
         *
         * The important things are:
         *
         * 1. DTOComparisonUtil wraps the parsing failure.
         * 2. Original Jackson exception remains available.
         */

        Assert.assertNotNull(
                error.getCause(),
                "Original JSON parsing exception should be preserved"
        );

        Assert.assertTrue(
                error.getCause()
                        instanceof JsonProcessingException,
                "Cause should be a Jackson JsonProcessingException"
        );
    }


    /*
     * =========================================================
     * 21. ROOT JSON TYPE MISMATCH
     *
     * Expected DTO produces OBJECT.
     *
     * Actual response is ARRAY.
     * =========================================================
     */

    @Test
    public void shouldDetectRootTypeMismatch() {

        String actualJson = """
                []
                """;

        ComparisonResult result =
                DTOComparisonUtil.compare(
                        createExpectedResponse(),
                        actualJson,
                        ComparisonOptions.defaultOptions()
                );

        Assert.assertFalse(
                result.isMatch()
        );

        Assert.assertEquals(
                result.getDifferences().size(),
                1
        );

        Assert.assertEquals(
                result.getDifferences()
                        .get(0)
                        .getDifferenceType(),
                DifferenceType.TYPE_MISMATCH
        );
    }


    /*
     * =========================================================
     * HELPER - EXPECTED DTO
     * =========================================================
     */

    private BookingResponse createExpectedResponse() {

        return BookingResponse.builder()
                .bookingid(1234)
                .booking(
                        Booking.builder()
                                .firstname("Deepank")
                                .lastname("Tiwari")
                                .totalprice(500)
                                .depositpaid(true)
                                .comment(null)
                                .bookingdates(
                                        BookingDates.builder()
                                                .checkin(
                                                        "2026-09-23"
                                                )
                                                .checkout(
                                                        "2026-09-25"
                                                )
                                                .build()
                                )
                                .items(
                                        List.of(
                                                Item.builder()
                                                        .id(1)
                                                        .name("A")
                                                        .build(),

                                                Item.builder()
                                                        .id(2)
                                                        .name("B")
                                                        .build()
                                        )
                                )
                                .build()
                )
                .build();
    }


    /*
     * =========================================================
     * HELPER - CREATE CLEAN ACTUAL JSON TREE
     * =========================================================
     */

    private ObjectNode createMatchingActualNode() {

        try {

            return (ObjectNode)
                    OBJECT_MAPPER.readTree(
                            MATCHING_ACTUAL_JSON
                    );

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Test baseline JSON is invalid",
                    e
            );
        }
    }


    /*
     * =========================================================
     * HELPER - GET BOOKING OBJECT
     * =========================================================
     */

    private ObjectNode getBooking(
            ObjectNode root) {

        return (ObjectNode)
                root.get(
                        "booking"
                );
    }


    /*
     * =========================================================
     * HELPER - GET ITEMS ARRAY
     * =========================================================
     */

    private ArrayNode getItems(
            ObjectNode root) {

        return (ArrayNode)
                getBooking(root)
                        .get(
                                "items"
                        );
    }


    /*
     * =========================================================
     * HELPER - EXPECT COMPARISON FAILURE
     * =========================================================
     */

    private AssertionError expectComparisonFailure(
            JsonNode actual) {

        return Assert.expectThrows(
                AssertionError.class,
                () ->
                        DTOComparisonUtil.compareAndThrow(
                                createExpectedResponse(),
                                actual.toString()
                        )
        );
    }


    /*
     * =========================================================
     * HELPER - ASSERT ERROR CONTENT
     * =========================================================
     */

    private void assertErrorContains(
            AssertionError error,
            String... expectedValues) {

        String message =
                error.getMessage();

        Assert.assertNotNull(
                message,
                "Comparison error message should not be null"
        );

        for (String expectedValue
                : expectedValues) {

            Assert.assertTrue(
                    message.contains(
                            expectedValue
                    ),
                    "Expected error message to contain: "
                            + expectedValue
                            + "\n\nActual message:\n"
                            + message
            );
        }
    }
}