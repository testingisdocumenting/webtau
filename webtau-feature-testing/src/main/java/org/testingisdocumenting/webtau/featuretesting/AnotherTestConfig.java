package org.testingisdocumenting.webtau.featuretesting;

import org.immutables.value.Value.Immutable;
import org.testingisdocumenting.webtau.cfg.Config;

import java.util.Map;

@Immutable
public interface AnotherTestConfig extends Config {
    Map<String, PersonaConfig> personas();

    @Immutable
    interface PersonaConfig {
        String password();

        static PersonaConfig withPassword(String password) {
            return ImmutablePersonaConfig.builder().password(password).build();
        }
    }
}
