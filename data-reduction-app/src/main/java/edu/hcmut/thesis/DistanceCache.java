package edu.hcmut.thesis;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DistanceCache {

    private static final Logger logger = Logger.getLogger(DistanceCache.class.getName());

    private static final DistanceCache instance = new DistanceCache();

    private DataCache dataCache = DataCache.getInstance();

    private LoadingCache<IdPair, BigDecimal> euclidCache;

    private LoadingCache<IdPair, BigDecimal> dtwCache;

    public static DistanceCache getInstance() {
        return instance;
    }

    private DistanceCache() {
        CacheLoader<IdPair, BigDecimal> euclidLoader = new CacheLoader<IdPair, BigDecimal>() {
            @Override
            public BigDecimal load(IdPair idPair) throws Exception {
                return DistanceService.getDistance(dataCache.get(idPair.getFromId()), dataCache.get(idPair.getToId()), MeasureMethod.EUCLID);
            }
        };
        euclidCache = CacheBuilder.newBuilder().build(euclidLoader);

        CacheLoader<IdPair, BigDecimal> dtwLoader = new CacheLoader<IdPair, BigDecimal>() {
            @Override
            public BigDecimal load(IdPair idPair) throws Exception {
//                long start = System.nanoTime();
                BigDecimal dtwDistance = DistanceService.getDistance(dataCache.get(idPair.getFromId()), dataCache.get(idPair.getToId()), MeasureMethod.DTW);
//                System.out.println("[" + idPair.getFromId() + ", " + idPair.getToId() +"]: " + dtwDistance.toString() + ". Took: " + (System.nanoTime() - start) + " ns");
                return dtwDistance;
            }
        };
        dtwCache = CacheBuilder.newBuilder().build(dtwLoader);
    }

    public BigDecimal getDistance(InstanceId fromId, InstanceId toId, MeasureMethod method) {
        try {
            if(MeasureMethod.EUCLID == method) {
                return euclidCache.get(new IdPair(fromId, toId));
            } else {
                return dtwCache.get(new IdPair(fromId, toId));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to get distance from " + fromId + " to " + toId + ", method=" + method.name(), e);
        }
        return BigDecimal.valueOf(Double.MAX_VALUE);
    }
}
