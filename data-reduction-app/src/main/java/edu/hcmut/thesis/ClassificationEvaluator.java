package edu.hcmut.thesis;

import java.util.List;
import java.util.Objects;

public class ClassificationEvaluator {

    public static double[] evaluate(List<TimeSeries> testData, MeasureMethod appliedMeasureMethod) {
        long testSize = testData.size();
        long correctSize = 0;
        for (TimeSeries test : testData) {
            String nearestClazz = test.getNearestInstanceInfo(appliedMeasureMethod).getClazz();
            if (Objects.equals(nearestClazz, test.getClazz())) {
                correctSize++;
            }
        }
        return new double[] { correctSize, testData.size(), ((double) correctSize / testSize) * 100 };
    }
}
