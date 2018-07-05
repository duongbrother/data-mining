package edu.hcmut.thesis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class NaiveRankReduction {

    private static DistanceCache distanceCache = DistanceCache.getInstance();

    private static KNNClassification knnClassification = KNNClassification.getInstance();

    public static LinkedHashSet<TimeSeries> reduce(List<TimeSeries> T) {
        // 1. Remove duplicate instances
        // 2. 1-NN
        long start = System.nanoTime();
        knnClassification.classify(T, MeasureMethod.DTW);
        // 3. Calculate Rank and Priority
        int N = T.size();
        LinkedHashSet<TimeSeries> S = new LinkedHashSet<>();
        int loopNum = 0;
        while (loopNum < N) {
            List<TimeSeries> T_S = T.stream().filter(ts -> !S.contains(ts)).collect(Collectors.toList());
            SortedMap<Long, List<TimeSeries>> ranks = new TreeMap<>(Comparator.naturalOrder());
            for (TimeSeries ts : T_S) {
                long rank = calculateRank(ts, T_S);
                List<TimeSeries> tss = CommonUtils.getOrCreate(rank, ranks, ArrayList.class);
                tss.add(ts);
            }
            List<TimeSeries> minRankInstances = ((TreeMap<Long, List<TimeSeries>>) ranks).firstEntry().getValue();
            if (minRankInstances.size() == 1) {
                S.add(minRankInstances.get(0));
            } else {
                SortedMap<Double, TimeSeries> priorities = new TreeMap<>(Comparator.naturalOrder());
                for (TimeSeries minRank : minRankInstances) {
                    double priority = calculatePriority(minRank, T_S);
                    priorities.put(priority, minRank);
                }
                TimeSeries minPriority = ((TreeMap<Double, TimeSeries>) priorities).firstEntry().getValue();
                S.add(minPriority);
            }
            loopNum++;
        }
        return S;
    }

    private static long calculateRank(TimeSeries ts, List<TimeSeries> data) {
        long rank = 0;
        for (TimeSeries timeSeries : data) {
            if (timeSeries.getId() == ts.getId()) {
                continue;
            }
            if (timeSeries.getNearestInstanceInfo(MeasureMethod.DTW).getId() == ts.getId()) {
                if (Objects.equals(timeSeries.getClazz(), ts.getClazz())) {
                    rank = rank + 1;
                } else {
                    rank = rank - 2;
                }
            }
        }
        return rank;
    }

    private static double calculatePriority(TimeSeries x, List<TimeSeries> data) {
        double priority = 0;
        for (TimeSeries xj : data) {
            if (xj.getId() == x.getId()) {
                continue;
            }
            if (xj.getNearestInstanceInfo(MeasureMethod.DTW).getId() == x.getId()) {
                priority += 1 / Math.pow(distanceCache.getDistance(x.getId(), xj.getId(), MeasureMethod.DTW).doubleValue(), 2);
            }
        }
        return priority;
    }
}
