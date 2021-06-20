/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.cli;

public class CliTestUtils {
    private static final String OS = System.getProperty("os.name");
    private static final boolean IS_WINDOWS = OS.toLowerCase().contains("win");

    public static void supportedPlatformOnly(Runnable code) {
        if (IS_WINDOWS) {
            return;
        }

        code.run();
    }

    public static void linuxOnly(Runnable code) {
        if (IS_WINDOWS) {
            return;
        }

        code.run();
    }
}
