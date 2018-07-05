package edu.hcmut.thesis;

import java.util.Objects;

public class IdPair {
    private InstanceId fromId;
    private InstanceId toId;

    public IdPair(InstanceId fromId, InstanceId toId) {
        this.fromId = fromId;
        this.toId = toId;
    }

    public InstanceId getFromId() {
        return fromId;
    }

    public void setFromId(InstanceId fromId) {
        this.fromId = fromId;
    }

    public InstanceId getToId() {
        return toId;
    }

    public void setToId(InstanceId toId) {
        this.toId = toId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdPair idPair = (IdPair) o;
        return (Objects.equals(this.fromId, idPair.fromId) && Objects.equals(this.toId, idPair.toId))
                || (Objects.equals(this.fromId, idPair.toId)  && Objects.equals(this.toId, idPair.fromId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId) + Objects.hash(toId);
    }

    @Override
    public String toString() {
        return "IdPair{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                '}';
    }
}
