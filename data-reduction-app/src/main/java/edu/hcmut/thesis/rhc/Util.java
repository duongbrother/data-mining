package edu.hcmut.thesis.rhc;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import edu.hcmut.thesis.InstanceId;
import edu.hcmut.thesis.TimeSeries;
import org.apache.commons.lang3.StringUtils;

public class Util {

    public static TimeSeries getCentroid(Collection<TimeSeries> data, long centroidId) {
        return getCentroid(data, centroidId, null);
    }

    public static TimeSeries getCentroid(Collection<TimeSeries> data, long centroidId, String centroidClazz) {
        TimeSeries centroid = new TimeSeries(new InstanceId(data.iterator().next().getId().getName() + "_Centroid", centroidId, data.iterator().next().getId().getType()));
        centroid.setClazz(StringUtils.isBlank(centroidClazz) ? data.iterator().next().getClazz() : centroidClazz);
        int minLength = data.stream().map(e -> e.getValues().size()).min(Integer::min).get();
        int size = data.size();
        for (int i = 0; i < minLength; i++) {
            double value = 0;
            for (TimeSeries xi : data) {
                value += xi.getValues().get(i);
            }
            centroid.addValue(value / size);
        }
        return centroid;
    }

    private static Map.Entry<String, Long> getBiggestClazz(Collection<TimeSeries> data) {
        SortedMap<String, Long> sortByClazz = new TreeMap<>(Comparator.naturalOrder());
        Map<String, Long> clazzToCount = data.stream().collect(Collectors.groupingBy(ts -> ts.getClazz(), Collectors.counting()));
        sortByClazz.putAll(clazzToCount);

        Map.Entry<String, Long> maxCountEntry = sortByClazz.entrySet().iterator().next();
        for (Map.Entry<String, Long> clazzCount : sortByClazz.entrySet()) {
            if(clazzCount.getValue() > maxCountEntry.getValue()) {
                maxCountEntry = clazzCount;
            }
        }
        System.out.println("Biggest Class is: " + maxCountEntry +". All=" + sortByClazz);
        return maxCountEntry;
    }

    public static boolean isHomogeneous(Cluster cluster) {
        return groupByClazz(cluster.getMembers()).size() <= 1;
    }

    public static Map<String, List<TimeSeries>> groupByClazz(Collection<TimeSeries> data) {
        return data.stream().collect(Collectors.groupingBy(TimeSeries::getClazz, Collectors.toList()));
    }
}
