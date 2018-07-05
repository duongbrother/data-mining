package edu.hcmut.thesis.drhc;

import edu.hcmut.thesis.ClassificationEvaluator;
import edu.hcmut.thesis.DistanceService;
import edu.hcmut.thesis.InstanceId;
import edu.hcmut.thesis.MeasureMethod;
import edu.hcmut.thesis.TimeSeries;
import edu.hcmut.thesis.rhc.Cluster;
import edu.hcmut.thesis.rhc.KMeans;
import edu.hcmut.thesis.rhc.Util;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class DRHC {

    public static List<TimeSeries> updateCS(List<TimeSeries> oldCS, List<TimeSeries> dataSeg) {
        /**
         * {Stage 1: Queue initialization}
         */
        Queue<Cluster> Q = new LinkedBlockingQueue<>();
        List<Cluster> CList = new ArrayList<>();
        for (TimeSeries prototype : oldCS) {
            CList.add(new Cluster(prototype, Arrays.asList(prototype)));
        }
        for (TimeSeries x : dataSeg) {
            x.setWeight(1);
            Cluster Cx = findCx(x, CList);
            Cx.addMember(x);
        }
        for (Cluster cluster : CList) {
            Q.add(cluster);
        }
        /**
         * {Stage 2: Construction of newCS}
         */
        int weightedMeanId = 0;
        List<TimeSeries> newCS = new ArrayList<>();
        while (!Q.isEmpty()) {
            Cluster C = Q.poll();
            Map<String, List<TimeSeries>> clazzes = Util.groupByClazz(C.getMembers());
            if (clazzes.size() == 1) {
                TimeSeries m = getWeightedMean(C.getMembers(), weightedMeanId);
                m.setWeight(C.getMembers().stream().map(ts -> ts.getWeight()).reduce(Double::sum).get());
                newCS.add(m);
                weightedMeanId++;
            } else {
                List<TimeSeries> M = new ArrayList<>();
                for (Map.Entry<String, List<TimeSeries>> clazzEntry : clazzes.entrySet()) {
                    weightedMeanId++;
                    TimeSeries mL = getWeightedMean(new LinkedHashSet<>(clazzEntry.getValue()), weightedMeanId);
                    mL.setWeight(clazzEntry.getValue().stream().map(ts -> ts.getWeight()).reduce(Double::sum).get());
                    M.add(mL);
                }
                List<Cluster> newClusters = KMeans.cluster(C.getMembers(), M);
                for (Cluster cluster : newClusters) {
                    Q.add(cluster);
                }
            }
        }
        return newCS;
    }

    private static Cluster findCx(TimeSeries x, List<Cluster> CList) {
        Cluster result = CList.iterator().next();
        BigDecimal minDis = DistanceService.getDistance(x, result.getCentroid(), MeasureMethod.EUCLID);
        for (int i = 1; i < CList.size(); i++) {
            BigDecimal distance = DistanceService.getDistance(x, CList.get(i).getCentroid(), MeasureMethod.EUCLID);
            if (minDis.compareTo(distance) > 0) {
                result = CList.get(i);
                minDis = distance;
            }
        }
        return result;
    }

    private static TimeSeries getWeightedMean(Set<TimeSeries> members, long weightedMeanId) {
        TimeSeries weightedMean = new TimeSeries(new InstanceId(members.iterator().next().getId().getName() + "_WeightedMean", weightedMeanId, members.iterator().next().getId().getType()));
        weightedMean.setClazz(members.iterator().next().getClazz());

        int minLength = members.stream().map(e -> e.getValues().size()).min(Integer::min).get();
        int size = members.size();
        for (int i = 0; i < minLength; i++) {
            double value = 0;
            double sumWeight = 0;
            for (TimeSeries xi : members) {
                value += (xi.getValues().get(i) * xi.getWeight());
                sumWeight += xi.getWeight();
            }
            weightedMean.addValue(value / sumWeight);
        }
        return weightedMean;
    }
}
