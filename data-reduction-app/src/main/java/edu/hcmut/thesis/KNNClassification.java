package edu.hcmut.thesis;

import java.math.BigDecimal;
import java.util.List;

public class KNNClassification {

    private DistanceCache distanceCache = DistanceCache.getInstance();

    private static final KNNClassification instance = new KNNClassification();

    private KNNClassification() {
    }

    public static KNNClassification getInstance() {
        return instance;
    }

    public void classify(List<TimeSeries> data, MeasureMethod measureMethod) {
        for (TimeSeries ts : data) {
            for (TimeSeries other : data) {
                if (other.getId() == ts.getId()) {
                    continue;
                }
                BigDecimal distance = distanceCache.getDistance(ts.getId(), other.getId(), measureMethod);
                if (ts.getNearestInstanceInfo(measureMethod) == null || ts.getNearestInstanceInfo(measureMethod).getDistance().compareTo(distance) > 0) {
                    ts.putNearestInstanceInfo(measureMethod, new NearestInstanceInfo(other.getId(), other.getClazz(), distance));
                }
            }
        }
    }

    public void classify(TimeSeries test, List<TimeSeries> trainSet, MeasureMethod measureMethod) {
        BigDecimal minDistance = BigDecimal.valueOf(Double.MAX_VALUE);
        for (TimeSeries ts : trainSet) {
            BigDecimal distance = distanceCache.getDistance(test.getId(), ts.getId(), measureMethod);
            if (distance.compareTo(minDistance) < 0) {
                minDistance = distance;
                test.putNearestInstanceInfo(measureMethod, new NearestInstanceInfo(ts.getId(), ts.getClazz(), distance));
            }
        }
    }

    public void classify(List<TimeSeries> testSet, List<TimeSeries> trainSet, MeasureMethod measureMethod) {
        for (TimeSeries test : testSet) {
            classify(test, trainSet, measureMethod);
        }
    }

    public void classifyWithDTW(List<TimeSeries> testSet, List<TimeSeries> trainSet, double windowSize) {
        for (TimeSeries test : testSet) {
            classifyWithDTW(test, trainSet, windowSize);
        }
    }

    public void classifyWithDTW(TimeSeries test, List<TimeSeries> trainSet, double windowSize) {
        BigDecimal minDistance = BigDecimal.valueOf(Double.MAX_VALUE);
        for (TimeSeries ts : trainSet) {
            BigDecimal distance = DistanceService.getDTWDistanceWithWindowSize(test.getValues(), ts.getValues(), windowSize);
            if (distance.compareTo(minDistance) < 0) {
                minDistance = distance;
                test.putNearestInstanceInfo(MeasureMethod.DTW, new NearestInstanceInfo(ts.getId(), ts.getClazz(), distance));
            }
        }
    }
}
