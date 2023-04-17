/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameConfig {
    private final String gameName;
    private final Path location;
    private final List<GameAchievement> achievements;

    public GameConfig(String gameName, Path location) {
        this.gameName = gameName;
        this.location = location;
        this.achievements = new ArrayList<>();
    }

    public void registerAchievement(String id, String name, String description) {
        achievements.add(new GameAchievement(id, name, description));
    }

    public String getGameName() {
        return gameName;
    }

    public Path getLocation() {
        return location;
    }

    public List<GameAchievement> getAchievements() {
        return Collections.unmodifiableList(achievements);
    }
}
