package com.Deepank.api.assertions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ComparisonResult {
    private final List<ComparisonDifference> differences = new ArrayList<>();

    public void addDifference(ComparisonDifference difference){
        differences.add(difference);
    }

    public boolean isMatch(){
        return differences.isEmpty();
    }
}
