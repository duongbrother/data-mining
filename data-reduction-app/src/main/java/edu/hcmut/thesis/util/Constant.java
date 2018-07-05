package edu.hcmut.thesis.util;

import java.util.HashMap;
import java.util.Map;

public class Constant {

    public static final Map<String, Integer> BEST_WINDOW_SIZES = new HashMap<>();
    static {
        BEST_WINDOW_SIZES.put("ArrowHead",           1);
        BEST_WINDOW_SIZES.put("Beef",                1);
        BEST_WINDOW_SIZES.put("BeetleFly",           2);
        BEST_WINDOW_SIZES.put("BirdChicken",         4);
        BEST_WINDOW_SIZES.put("CBF",                 17);
        BEST_WINDOW_SIZES.put("Car",                 7);
        BEST_WINDOW_SIZES.put("Coffee",              2);
        BEST_WINDOW_SIZES.put("DiatomSizeReduction", 14);
        BEST_WINDOW_SIZES.put("FISH",                5);
        BEST_WINDOW_SIZES.put("Gun_Point",           2);
        BEST_WINDOW_SIZES.put("Lighting2",           2);
        BEST_WINDOW_SIZES.put("Lighting7",           3);
        BEST_WINDOW_SIZES.put("Meat",                1);
        BEST_WINDOW_SIZES.put("Plane",               4);
        BEST_WINDOW_SIZES.put("Trace",               4);
    }

    public static int getBestWindowSize(String dataSetName) {
        Integer size = BEST_WINDOW_SIZES.get(dataSetName);
        if(size == null || size == 0) {
            return 4;
        }
        return size;
    }
}
