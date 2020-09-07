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

package org.testingisdocumenting.webtau.http.datanode;

import java.util.Objects;

public class DataNodeId {
    private String path;
    private String name;
    private int idx;

    public DataNodeId(String name) {
        this.name = name;
        this.path = name;
    }

    public DataNodeId(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public DataNodeId(String path, String name, int idx) {
        this(path, name);
        this.idx = idx;
    }

    public DataNodeId child(String name) {
        return new DataNodeId(path + "." + name, name);
    }

    public DataNodeId peer(int idx) {
        return new DataNodeId(path + "[" + idx + "]", name, idx);
    }

    public String getPath() {
        return path;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataNodeId that = (DataNodeId) o;
        return idx == that.idx &&
                Objects.equals(path, that.path) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, name, idx);
    }
}
