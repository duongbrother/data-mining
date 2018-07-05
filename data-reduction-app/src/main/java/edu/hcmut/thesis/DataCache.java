package edu.hcmut.thesis;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataCache {

    private final Map<InstanceId, TimeSeries> data = new LinkedHashMap<>();

    private static final DataCache instance = new DataCache();

    private DataCache() {
    }

    public static DataCache getInstance() {
        return instance;
    }

    public TimeSeries put(TimeSeries ts) {
        return data.put(ts.getId(), ts);
    }

    public TimeSeries get(InstanceId key) {
        return data.get(key);
    }

    public Map<InstanceId, TimeSeries> asMap() {
        return data;
    }
}
