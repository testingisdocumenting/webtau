/*
 * Copyright 2020 webtau maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.webtau.browser;

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;

import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.cfg.ConfigValue.declare;
import static org.testingisdocumenting.webtau.cfg.ConfigValue.declareBoolean;

public class BrowserConfig implements WebTauConfigHandler {
    private static final Supplier<Object> NO_DEFAULT = () -> null;

    private static final ConfigValue windowWidth = declare("windowWidth", "browser window width", () -> 1000);
    private static final ConfigValue windowHeight = declare("windowHeight", "browser window height", () -> 800);
    private static final ConfigValue headless = declareBoolean("headless", "run headless mode");
    private static final ConfigValue chromeDriverPath = declare("chromeDriverPath", "path to chrome driver binary", NO_DEFAULT);
    private static final ConfigValue chromeBinPath = declare("chromeBinPath", "path to chrome binary", NO_DEFAULT);

    public static int getWindowWidth() {
        return windowWidth.getAsInt();
    }

    public static int getWindowHeight() {
        return windowHeight.getAsInt();
    }

    public static boolean isHeadless() {
        return headless.getAsBoolean();
    }

    public static Path getChromeBinPath() {
        return chromeBinPath.getAsPath();
    }

    public static Path getChromeDriverPath() {
        return chromeDriverPath.getAsPath();
    }

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(
                windowWidth,
                windowHeight,
                headless,
                chromeDriverPath,
                chromeBinPath);
    }
}
