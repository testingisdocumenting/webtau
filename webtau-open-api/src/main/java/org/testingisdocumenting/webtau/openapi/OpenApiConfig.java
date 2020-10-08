package org.testingisdocumenting.webtau.openapi;

import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

import java.util.Optional;

public interface OpenApiConfig {
    Optional<String> specUrl();

    @Value.Default
    default boolean ignoreAdditionalProperties() {
        return false;
    }

    @Immutable
    interface DefaultOpenApiConfig extends OpenApiConfig {}

    OpenApiConfig DEFAULT_CFG = ImmutableDefaultOpenApiConfig.builder().build();
}
