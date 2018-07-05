package edu.hcmut.thesis;

import java.util.Objects;

public class InstanceId {

    private String name;

    private long id;

    private Type type;

    public InstanceId(String name, long id, Type type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceId that = (InstanceId) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                type == that.type;
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, id, type);
    }

    @Override
    public String toString() {
        return "InstanceId{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", type=" + type +
                '}';
    }

    public static enum Type {
        TRAIN, TEST;
    }
}
