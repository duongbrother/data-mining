package edu.hcmut.thesis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TimeSeries {
    private InstanceId id;
    private String clazz;
    private List<Double> values = new ArrayList<>();
    private Map<MeasureMethod, NearestInstanceInfo> nearestInstances = new ConcurrentHashMap<>();
    private double weight;
    private boolean isPrototype = false;

    public TimeSeries(InstanceId id) {
        this(id, null);
    }

    public TimeSeries(InstanceId id, String clazz) {
        this.id = id;
        this.clazz = clazz;
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

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    public void addValue(Double value) {
        values.add(value);
    }

    public NearestInstanceInfo getNearestInstanceInfo(MeasureMethod measureMethod) {
        return nearestInstances.get(measureMethod);
    }

    public void putNearestInstanceInfo(MeasureMethod measureMethod, NearestInstanceInfo info) {
        nearestInstances.put(measureMethod, info);
    }

    public Map<MeasureMethod, NearestInstanceInfo> getNearestInstances() {
        return nearestInstances;
    }

    public void setNearestInstances(Map<MeasureMethod, NearestInstanceInfo> nearestInstances) {
        this.nearestInstances = nearestInstances;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isPrototype() {
        return isPrototype;
    }

    public void setPrototype(boolean prototype) {
        isPrototype = prototype;
    }

    @Override
    public String toString() {
        return "{" + toStringFields() + "}";
    }

    protected StringBuilder toStringFields() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id);
        sb.append(", clazz=").append(clazz);
        sb.append(", values=").append(values);
        sb.append(", nearestInstances=").append(nearestInstances);
        sb.append(", weight=").append(weight);
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TimeSeries that = (TimeSeries) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clazz, values);
    }
}
