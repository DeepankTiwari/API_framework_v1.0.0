package com.Deepank.api.enums.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthServiceUrl {

    AUTH("/auth");

    public final String path;
}