package com.Deepank.api.assertions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.LinkedHashSet;
import java.util.Set;

public final class DTOComparisonUtil {
    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper();

    private DTOComparisonUtil() {
    }

    public static void compareAndThrow(
            Object expectedDto,
            String actualJson) {
         compareAndThrow(
                expectedDto,
                actualJson,
                ComparisonOptions.defaultOptions()
        );
    }
    public static void compareAndThrow(
            Object expectedDto,
            String actualJson,
            ComparisonOptions options) {

            ComparisonResult result = compare(expectedDto, actualJson, options);

            if (!result.isMatch()){
                throw new AssertionError(buildFailureMessage(result));
            }
    }

    public static ComparisonResult compare(Object expectedDto, String actualJson, ComparisonOptions options){
        try {
            JsonNode expectedNode = OBJECT_MAPPER.valueToTree(expectedDto);
            JsonNode actualNode = OBJECT_MAPPER.readTree(actualJson);

            ComparisonResult result = new ComparisonResult();
            compareNodes(
                    "",
                    expectedNode,
                    actualNode,
                    options,
                    result
            );
            return result;
        }catch(JsonProcessingException e){
            throw new IllegalArgumentException("Unable to parse actual response string", e);
        }
    }

    private static void compareNodes(String path, JsonNode expectedNode, JsonNode actualNode, ComparisonOptions options, ComparisonResult result){
        if(shouldIgnorePath(path, options)){
            return;
        }
        if (isNull(expectedNode) && isNull(actualNode)){
            return;
        }
        if (isNull(expectedNode) || isNull(actualNode)){
            result.addDifference(ComparisonDifference.builder()
                            .path(path)
                            .differenceType(DifferenceType.NULL_VS_NON_NULL)
                            .expected(expectedNode)
                            .actual(actualNode)
                            .message("Expected and actual nullability differs")
                            .build());
            return;
        }
        if (expectedNode.getNodeType() != actualNode.getNodeType()){
            result.addDifference(ComparisonDifference.builder()
                            .path(path)
                            .differenceType(DifferenceType.TYPE_MISMATCH)
                            .expected(expectedNode)
                            .actual(actualNode)
                            .message("Expected type " + expectedNode.getNodeType()
                            + " but actual type " + actualNode.getNodeType())
                            .build());
            return;
        }
        if (expectedNode.isObject()){
            compareObjects(path, expectedNode, actualNode, options, result);
            return;
        }
        if (expectedNode.isArray()){
            compareArray(path, expectedNode, actualNode, options, result);
            return;
        }
        compareValues(path, expectedNode, actualNode, options, result);
    }

    private static void compareObjects(String path, JsonNode expectedNode, JsonNode actualNode, ComparisonOptions options, ComparisonResult result){
        Set<String> allFields = new LinkedHashSet<>();
        expectedNode.fieldNames().forEachRemaining(allFields::add);
        actualNode.fieldNames().forEachRemaining(allFields::add);

        for (String field : allFields){
            String currentPath = buildPath(path, field);
            if(shouldIgnorePath(currentPath, options)){
                continue;
            }
            boolean expectedHasField = expectedNode.has(field);
            boolean actualHasField = actualNode.has(field);
            if (expectedHasField && !actualHasField){
                result.addDifference(ComparisonDifference.builder()
                                .path(currentPath)
                                .differenceType(DifferenceType.MISSING_FIELD)
                                .expected(expectedNode.get(field))
                                .actual(null)
                                .message("Field is missing in actual response")
                                .build());
                continue;
            }
            if (!expectedHasField && actualHasField) {
                result.addDifference(ComparisonDifference.builder()
                                .path(currentPath)
                                .differenceType(DifferenceType.EXTRA_FIELD)
                                .expected(null)
                                .actual(actualNode.get(field))
                                .message("Unexpected field found in actual response")
                                .build());
                continue;
            }
            compareNodes(currentPath, expectedNode.get(field), actualNode.get(field), options, result);
        }
    }

    private static void compareArray(String path, JsonNode expectedNode, JsonNode actualNode, ComparisonOptions options, ComparisonResult result){
        if (expectedNode.size() != actualNode.size()){
            result.addDifference(ComparisonDifference.builder()
                            .path(path)
                            .differenceType(DifferenceType.ARRAY_SIZE_MISMATCH)
                            .expected(expectedNode)
                            .actual(actualNode)
                            .message("Expected array size " + expectedNode.size() + " but actual array size " + actualNode.size())
                            .build());
        }
        int commonSize = Math.min(expectedNode.size(), actualNode.size());
        for(int i=0; i<commonSize; i++){
            String currentPath = path + "[" + i + "]";
            compareNodes(currentPath, expectedNode.get(i), actualNode.get(i), options, result);
        }
    }

    private static void compareValues(String path, JsonNode expectedNode, JsonNode actualNode, ComparisonOptions options, ComparisonResult result){
        if(!expectedNode.equals(actualNode)){
            result.addDifference(ComparisonDifference.builder()
                            .path(path)
                            .differenceType(DifferenceType.VALUE_MISMATCH)
                            .expected(expectedNode)
                            .actual(actualNode)
                            .message("Expected " + expectedNode + " but found " + actualNode)
                            .build());
        }
    }

    private static String buildFailureMessage(ComparisonResult result){
        StringBuilder builder = new StringBuilder();
        builder.append("DTO comparison failed. ")
                .append(result.getDifferences().size())
                .append(" difference(s) found.\n");
        int index = 1;
        for (ComparisonDifference difference : result.getDifferences()){
            builder.append("\n")
                    .append(index++)
                    .append(". Path: ").append(difference.getPath())
                    .append("\n   Type: ").append(difference.getDifferenceType())
                    .append("\n   Expected: ").append(difference.getExpected())
                    .append("\n   Actual: ").append(difference.getActual())
                    .append("\n   Message: ").append(difference.getMessage())
                    .append("\n");
        }
        return builder.toString();
    }

    private static boolean shouldIgnorePath(String path, ComparisonOptions options){
        if(path == null || path.isBlank()){
            return false;
        }
        return options.getIgnoreFields().contains(path);
    }

    private static String buildPath(String parent, String field){
        if (parent == null || parent.isBlank()){
            return field;
        }
        return parent + "." + field;
    }

    private static boolean isNull(JsonNode node){
        return node == null || node.isNull();
    }
}
