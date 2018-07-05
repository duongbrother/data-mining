package edu.hcmut.thesis.drhc;

import edu.hcmut.thesis.TimeSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataSegmentUtil {

    public static List<List<TimeSeries>> createDataSegments(List<TimeSeries> trainData, int baseTrainPercent, int nbSegments) {
        List<List<TimeSeries>> result = new ArrayList<>();
        int fullTrainSize = trainData.size();
        int trainSize = (baseTrainPercent * fullTrainSize) / 100;
        int segmentSize = (fullTrainSize - trainSize) / nbSegments;
        Set<Integer> randomValues = new HashSet<>();
        for (int segIndex = 0; segIndex < nbSegments; segIndex++) {
            List<TimeSeries> segment = new ArrayList<>();
            while (segment.size() < segmentSize) {
                int randomIndex = ThreadLocalRandom.current().nextInt(0, fullTrainSize - 1);
                if (randomValues.add(randomIndex)) {
                    segment.add(trainData.remove(randomIndex));
                }
            }
            result.add(segment);
        }
        return result;
    }

//    public static List<List<TimeSeries>> createBaseTrainsetAndDataSegments(List<TimeSeries> trainData, int baseTrainPercent, int segmentPercent) {
//        List<List<TimeSeries>> result = new ArrayList<>();
//        int fullTrainSize = trainData.size();
//        int baseTrainSize = (baseTrainPercent * fullTrainSize) / 100;
//        int segmentSize = (segmentPercent * fullTrainSize) / 100;
//        result.add(trainData.subList(0, baseTrainSize - 1));
//        int nrow = baseTrainSize;
//        while(nrow < fullTrainSize) {
//            if(nrow + segmentSize < fullTrainSize) {
//                result.add(trainData.subList(nrow, nrow + segmentSize - 1));
//                nrow += segmentSize;
//            } else {
//                result.add(trainData.subList(nrow, fullTrainSize - 1));
//                nrow = fullTrainSize;
//            }
//        }
//
//        return result;
//    }

    public static <T> List<List<T>> createBaseTrainsetAndDataSegments(List<T> trainData, int baseTrainPercent, int segmentPercent) {
        List<List<T>> result = new ArrayList<>();
        int fullTrainSize = trainData.size();
        int baseTrainSize = (baseTrainPercent * fullTrainSize) / 100;
        int segmentSize = (segmentPercent * fullTrainSize) / 100;
        result.add(trainData.subList(0, baseTrainSize));
        int nrow = baseTrainSize;
        while (nrow < fullTrainSize) {
            if (nrow + segmentSize < fullTrainSize) {
                if (nrow + 1.7 * segmentSize > fullTrainSize) {
                    result.add(trainData.subList(nrow, fullTrainSize));
                    nrow = fullTrainSize;
                } else {
                    result.add(trainData.subList(nrow, nrow + segmentSize));
                    nrow += segmentSize;
                }
            } else {
                result.add(trainData.subList(nrow, fullTrainSize));
                nrow = fullTrainSize;
            }
        }

        return result;
    }

    public static List<Integer> getRandomList(int upperBound, int nbRandomValues) {
        Set<Integer> randomValues = new HashSet<>();
        while (randomValues.size() < nbRandomValues) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, upperBound);
            randomValues.add(randomNum);
        }
        return new ArrayList<>(randomValues);
    }

    public static List<List<Integer>> createIntegerSegments(List<Integer> trainData, int baseTrainPercent, int nbSegments) {
        List<List<Integer>> result = new ArrayList<>();
        int fullTrainSize = trainData.size();
        int trainSize = (baseTrainPercent * fullTrainSize) / 100;
        int segmentSize = (fullTrainSize - trainSize) / nbSegments;
        Set<Integer> randomValues = new HashSet<>();
        for (int segIndex = 0; segIndex < nbSegments; segIndex++) {
            List<Integer> segment = new ArrayList<>();
            while (segment.size() < segmentSize) {
                int randomIndex = ThreadLocalRandom.current().nextInt(0, trainData.size() - 1);
                if (randomValues.add(randomIndex)) {
                    segment.add(trainData.remove(randomIndex));
                }
            }
            result.add(segment);
        }
        return result;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<Integer> range = IntStream.rangeClosed(0, 1070).boxed().collect(Collectors.toList());
//        List<List<Integer>> segement = createIntegerSegments(range, 50, 10);
//        for (List<Integer> integers : segement) {
//            Collections.sort(integers);
//            System.out.println("Segment " + integers.size() + ": " + integers);
//        }
        List<List<Integer>> baseTrainsetAndDataSegments = createBaseTrainsetAndDataSegments(range, 50, 10);
        for (List<Integer> baseTrainsetAndDataSegment : baseTrainsetAndDataSegments) {
            System.out.println("Segment: " + baseTrainsetAndDataSegment.size() + ": " + baseTrainsetAndDataSegment);
        }
        System.out.println("Train " + range.size() + ": " + range);
        System.out.println("Spent time: " + (System.currentTimeMillis() - start) + " ms");
    }
}
