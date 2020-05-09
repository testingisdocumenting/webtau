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
    private static final String SET_SOURCE = "manual";

    static final ConfigValue dbPrimaryUrl = declare("dbUrl",
            "primary database url", () -> "");

    static final ConfigValue dbPrimaryDriverClassName = declare("dbDriverClassName",
            "primary database driver class name", () -> "");

    static final ConfigValue dbPrimaryUserName = declare("dbUserName",
            "primary database user name", () -> "");

    static final ConfigValue dbPrimaryPassword = declare("dbPassowrd",
            "primary database password", () -> "");

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(dbPrimaryUrl, dbPrimaryDriverClassName, dbPrimaryUserName, dbPrimaryPassword);
    }

    public static void reset() {
        dbPrimaryUrl.reset();
        dbPrimaryDriverClassName.reset();
        dbPrimaryUserName.reset();
        dbPrimaryPassword.reset();
    }

    public static void setDbPrimaryUrl(String url) {
        dbPrimaryUrl.set(SET_SOURCE, url);
    }

    public static void setDbPrimaryDriverClassName(String className) {
        dbPrimaryDriverClassName.set(SET_SOURCE, className);
    }

    public static void setDbPrimaryUserName(String userName) {
        dbPrimaryUserName.set(SET_SOURCE, userName);
    }

    public static void setDbPrimaryPassword(String password) {
        dbPrimaryPassword.set(SET_SOURCE, password);
    }

    public static String getDbPrimaryUrl() {
        return dbPrimaryUrl.getAsString();
    }

    public static String getDbPrimaryDriverClassName() {
        return dbPrimaryDriverClassName.getAsString();
    }

    public static String getDbPrimaryUserName() {
        return dbPrimaryUserName.getAsString();
    }

    public static String getDbPrimaryPassword() {
        return dbPrimaryPassword.getAsString();
    }

    public static boolean isSet() {
        return !dbPrimaryUrl.isDefault();
    }
}
