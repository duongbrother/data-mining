package edu.hcmut.thesis;

import java.math.BigDecimal;
import java.util.Objects;

public class NearestInstanceInfo {
    private InstanceId id;
    private String clazz;
    private BigDecimal distance;

    public NearestInstanceInfo(InstanceId id, String clazz, BigDecimal distance) {
        this.id = id;
        this.clazz = clazz;
        this.distance = distance;
    }

    public InstanceId getId() {
        return id;
    }

    public void setId(InstanceId id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NearestInstanceInfo that = (NearestInstanceInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(clazz, that.clazz) &&
                Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, clazz, distance);
    }

    @Override
    public String toString() {
        return "NearestInstanceInfo{" +
                "id=" + id +
                ", clazz='" + clazz + '\'' +
                ", distance=" + distance +
                '}';
    }
}
