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

package org.testingisdocumenting.webtau.db.cfg;

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;

import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.cfg.ConfigValue.declare;

public class DbConfig implements WebTauConfigHandler {
    static final ConfigValue dbPrimaryUrl = declare("dbUrl",
            "primary database url", () -> "");

    static final ConfigValue dbPrimaryDriver = declare("dbDriverClassName",
            "primary database driver class name", () -> "");

    static final ConfigValue dbPrimaryUserName = declare("dbUserName",
            "primary database user name", () -> "");

    static final ConfigValue dbPrimaryPassword = declare("dbPassowrd",
            "primary database password", () -> "");

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(dbPrimaryUrl, dbPrimaryDriver, dbPrimaryUserName, dbPrimaryPassword);
    }
}
