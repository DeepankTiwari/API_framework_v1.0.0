package com.Deepank.api.assertions;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class ComparisonOptions {

    @Singular("ignoreField")
    Set<String> ignoreFields;

    public static ComparisonOptions defaultOptions(){
        return ComparisonOptions.builder().build();
    }
}
