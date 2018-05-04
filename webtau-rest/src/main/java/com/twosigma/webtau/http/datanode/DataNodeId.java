package com.twosigma.webtau.http.datanode;

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
}
