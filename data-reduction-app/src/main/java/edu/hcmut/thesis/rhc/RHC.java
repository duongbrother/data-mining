package edu.hcmut.thesis.rhc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import edu.hcmut.thesis.TimeSeries;

public class RHC {

    public static List<TimeSeries> reduce(List<TimeSeries> TS) {
        // Stage 1: Queue Initialization
        Queue<Cluster> queue = new LinkedBlockingQueue<>();
        int centroidId = 0;
        queue.add(new Cluster(Util.getCentroid(TS, centroidId), new LinkedHashSet<>(TS)));
        // Stage 2: Construction of the condensing set
        List<TimeSeries> CS = new ArrayList<>();
        while (!queue.isEmpty()) {
            Cluster C = queue.poll();
            Map<String, List<TimeSeries>> clazzes = Util.groupByClazz(C.getMembers());
            if (clazzes.size() == 1) {
                // C is homogeneous
                TimeSeries prototype = C.getCentroid();
                prototype.setWeight(clazzes.entrySet().iterator().next().getValue().size());
                prototype.setPrototype(true);
                CS.add(prototype);
            } else if(clazzes.size() > 1) {
                // C is non-homogeneous
                List<TimeSeries> means = new ArrayList<>();
                for (Map.Entry<String, List<TimeSeries>> clazzEntry : clazzes.entrySet()) {
                    centroidId++;
                    TimeSeries centroid = Util.getCentroid(clazzEntry.getValue(), centroidId, clazzEntry.getKey());
                    means.add(centroid);
                }
                List<Cluster> newClusters = KMeans.cluster(C.getMembers(), means);
                for (Cluster NC : newClusters) {
                    if(NC.getMembers().size() > 0) {
                        queue.add(NC);
                    }
                }
            }
        }
        return CS;
    }
}
