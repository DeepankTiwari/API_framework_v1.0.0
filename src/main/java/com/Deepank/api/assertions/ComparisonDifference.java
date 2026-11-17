package com.Deepank.api.assertions;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComparisonDifference {
    private String path;
    private DifferenceType differenceType;
    private JsonNode expected;
    private JsonNode actual;
    private String message;
}
