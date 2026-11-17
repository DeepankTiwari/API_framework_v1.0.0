package com.Deepank.api.models.bookingSvc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingTestData {

    public List<BookingScenario> createScenarios;
    public List<BookingScenario> updateScenarios;

    @Data
    @Builder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingScenario {
        private String scenarioName;
        private String firstname;
        private String lastname;
        private Integer totalprice;
        private Boolean depositpaid;
        private String checkin;
        private String checkout;
        private String additionalneeds;
    }
}
