package edu.hcmut.thesis.rhc;

import edu.hcmut.thesis.DistanceService;
import edu.hcmut.thesis.MeasureMethod;
import edu.hcmut.thesis.TimeSeries;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KMeans {

    public static List<Cluster> cluster(Collection<TimeSeries> data, List<TimeSeries> centroids) {
        Map<Integer, Cluster> clusters = new HashMap<>();
        for (int i = 0; i < centroids.size(); i++) {
            clusters.put(i, new Cluster(centroids.get(i)));
        }
        for (TimeSeries instance : data) {
            BigDecimal minDistance = BigDecimal.valueOf(Double.MAX_VALUE);
            int clusterIndex = 0;
            for (int i = 0; i < centroids.size(); i++) {
                BigDecimal distance = DistanceService.getDistance(instance, centroids.get(i), MeasureMethod.EUCLID);
                if (distance.compareTo(minDistance) < 0) {
                    minDistance = distance;
                    clusterIndex = i;
                }
            }
            clusters.get(clusterIndex).addMember(instance);
        }
        return new ArrayList<>(clusters.values());
    }
}
