/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.http.datanode;

public class DataNodeId {
    private final String path;
    private final String name;
    private final String normalizedPath; // name without array index
    private int idx;

    public DataNodeId(String name) {
        this.name = name;
        this.path = name;
        this.normalizedPath = name;
    }

    private DataNodeId(String path, String normalizedPath, String name) {
        this.path = path;
        this.name = name;
        this.normalizedPath = normalizedPath;
    }

    private DataNodeId(String path, String normalizedPath, String name, int idx) {
        this(path, normalizedPath, name);
        this.idx = idx;
    }

    public DataNodeId child(String name) {
        String newPath = path + "." + name;
        return new DataNodeId(newPath, newPath, name);
    }

    public DataNodeId peer(int idx) {
        return new DataNodeId(path + "[" + idx + "]", path, name, idx);
    }

    public String getPath() {
        return path;
    }

    public String getNormalizedPath() {
        return normalizedPath;
    }

    public String getName() {
        return name;
    }

    public int getIdx() {
        return idx;
    }

    @Override
    public String toString() {
        return path;
    }
}
