package edu.hcmut.thesis;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import edu.hcmut.thesis.rhc.RHC;
import edu.hcmut.thesis.util.Constant;
import edu.hcmut.thesis.util.LatexUtil;

public class App {
    public static void main(String[] args) {
        List<String> dataNames = Arrays.asList("ArrowHead", "Beef", "BeetleFly", "BirdChicken", "Car", "CBF", "Coffee", "DiatomSizeReduction", "FISH", "Gun_Point", "Lighting2", "Lighting7", "Meat", "Plane", "Trace");
//        List<String> fullDataNames = Arrays.asList("50words", "Adiac", "ArrowHead", "Beef", "BeetleFly", "BirdChicken", "Car", "CBF", "ChlorineConcentration", "CinC_ECG_torso", "Coffee", "Computers", "Cricket_X", "Cricket_Y", "Cricket_Z", "DiatomSizeReduction", "DistalPhalanxOutlineAgeGroup", "DistalPhalanxOutlineCorrect", "DistalPhalanxTW", "Earthquakes", "ECG200", "ECG5000", "ECGFiveDays", "ElectricDevices", "FaceAll", "FaceFour", "FacesUCR", "FISH", "FordA", "FordB", "Gun_Point", "Ham", "HandOutlines", "Haptics", "Herring", "InlineSkate", "InsectWingbeatSound", "ItalyPowerDemand", "LargeKitchenAppliances", "Lighting2", "Lighting7", "MALLAT", "Meat", "MedicalImages", "MiddlePhalanxOutlineAgeGroup", "MiddlePhalanxOutlineCorrect", "MiddlePhalanxTW", "MoteStrain", "name.txt", "NonInvasiveFatalECG_Thorax1", "NonInvasiveFatalECG_Thorax2", "OliveOil", "OSULeaf", "PhalangesOutlinesCorrect", "Phoneme", "Plane", "ProximalPhalanxOutlineAgeGroup", "ProximalPhalanxOutlineCorrect", "ProximalPhalanxTW", "RefrigerationDevices", "ScreenType", "ShapeletSim", "ShapesAll", "SmallKitchenAppliances", "SonyAIBORobotSurface", "SonyAIBORobotSurfaceII", "StarLightCurves", "Strawberry", "SwedishLeaf", "Symbols", "synthetic_control", "ToeSegmentation1", "ToeSegmentation2", "Trace", "TwoLeadECG", "Two_Patterms", "UWaveGestureLibraryAll", "uWaveGestureLibrary_X", "uWaveGestureLibrary_Y", "uWaveGestureLibrary_Z", "wafer", "Wine", "WordsSynonyms", "Worms", "WormsTwoClass", "yoga");
//        List<String> dataNames = Arrays.asList("ElectricDevices", "FaceAll", "FaceFour", "FacesUCR", "FISH", "FordA", "FordB", "Gun_Point", "Ham", "HandOutlines", "Haptics", "Herring", "InlineSkate", "InsectWingbeatSound", "ItalyPowerDemand", "LargeKitchenAppliances", "Lighting2", "Lighting7", "MALLAT", "Meat", "MedicalImages", "MiddlePhalanxOutlineAgeGroup", "MiddlePhalanxOutlineCorrect", "MiddlePhalanxTW", "MoteStrain", "name.txt", "NonInvasiveFatalECG_Thorax1", "NonInvasiveFatalECG_Thorax2", "OliveOil", "OSULeaf", "PhalangesOutlinesCorrect", "Phoneme", "Plane", "ProximalPhalanxOutlineAgeGroup", "ProximalPhalanxOutlineCorrect", "ProximalPhalanxTW", "RefrigerationDevices", "ScreenType", "ShapeletSim", "ShapesAll", "SmallKitchenAppliances", "SonyAIBORobotSurface", "SonyAIBORobotSurfaceII", "StarLightCurves", "Strawberry", "SwedishLeaf", "Symbols", "synthetic_control", "ToeSegmentation1", "ToeSegmentation2", "Trace", "TwoLeadECG", "Two_Patterms", "UWaveGestureLibraryAll", "uWaveGestureLibrary_X", "uWaveGestureLibrary_Y", "uWaveGestureLibrary_Z", "wafer", "Wine", "WordsSynonyms", "Worms", "WormsTwoClass", "yoga");
//        List<String> dataNames = Arrays.asList("Coffee");
        classifyWithThreeDifferentStrategies(dataNames);
//        findTheBestWindowSize(dataNames);
//        printDatasetInfo(fullDataNames);
    }

    private static void printDatasetInfo(List<String> dataNames) {
        Map<String, Integer> trainsize = new HashMap<>();
        for (String dataName : dataNames) {
            try {
                System.out.println("Reading " + dataName);
                List<TimeSeries> trainData = DataReader.readDataFile("data/" + dataName + "/" + dataName + "_TRAIN", dataName, InstanceId.Type.TRAIN);
                List<TimeSeries> testData = DataReader.readDataFile("data/" + dataName + "/" + dataName + "_TEST", dataName, InstanceId.Type.TEST);
                DataSet ds = new DataSet(dataName, trainData, testData);
//                System.out.println(ds.getName() + ": " + ds.getTrainData().size());
                trainsize.put(dataName, ds.getTrainData().size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Map.Entry<String, Integer>> collect = trainsize.entrySet().stream().filter(e -> e.getValue() >= 100).collect(Collectors.toList());
        collect.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue() - o2.getValue();
            }
        });
        System.out.println(collect);

    }

    private static void classifyWithThreeDifferentStrategies(List<String> dataNames) {
        Map<String, List<ClassificationResult>> results = new LinkedHashMap<>();
        for (String dataName : dataNames) {
            List<ClassificationResult> subResult = new ArrayList<>();
            try {
                List<TimeSeries> trainData = DataReader.readDataFile("data/" + dataName + "/" + dataName + "_TRAIN", dataName, InstanceId.Type.TRAIN);
                List<TimeSeries> testData = DataReader.readDataFile("data/" + dataName + "/" + dataName + "_TEST", dataName, InstanceId.Type.TEST);
                DataSet ds = new DataSet(dataName, trainData, testData);
                System.out.println("-------------" + dataName + "(" + trainData.size() + "/" + testData.size() + ")-------------");
                /**
                 * _ 1. KNN uses DTW
                 */
                ClassificationResult resultWithOriginalTrainSet = classifyKNNWithOrginalTrainSet(ds);
                System.out.println(resultWithOriginalTrainSet);
                subResult.add(resultWithOriginalTrainSet);
                /**
                 * _ 2. KNN after reducing TRAIN set by RHC
                 */
                ClassificationResult resultWithRHCTrainSet = classifyKNNWithRHCTrainSet(ds);
                subResult.add(resultWithRHCTrainSet);
                System.out.println(resultWithRHCTrainSet);

                /**
                 * _ 3. KNN after reducing TRAIN set by Naive Rank
                 */
//                ClassificationResult resultWithNaiveRankTrainSet = classifyKNNWithNaiveRankTrainSet(ds, resultWithRHCTrainSet.getReducedTrainSize());
                ClassificationResult resultWithNaiveRankTrainSet = classifyKNNWithNaiveRankTrainSet(ds, new Double(60 * ds.getTrainData().size() / 100d).intValue());
                System.out.println(resultWithNaiveRankTrainSet);
                subResult.add(resultWithNaiveRankTrainSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
            results.put(dataName, subResult);
        }
        LatexUtil.buildSummaryTable(results);
        LatexUtil.buildTimeTable(results);
    }

    private static ClassificationResult classifyKNNWithNaiveRankTrainSet(DataSet ds, int keepSize) {
        long start = System.currentTimeMillis();
        ClassificationResult result = new ClassificationResult();
        long startNaive = System.currentTimeMillis();
        LinkedHashSet<TimeSeries> reduce = NaiveRankReduction.reduce(ds.getTrainData());
        long spendTimeForReduction = System.currentTimeMillis() - startNaive;
        List<TimeSeries> reducedNaiveTrainData = new ArrayList<>(reduce).subList(0, keepSize);
        reducedNaiveTrainData.stream().forEach(e -> DataCache.getInstance().put(e));
        long startClassify = System.currentTimeMillis();
        KNNClassification.getInstance().classifyWithDTW(ds.getTestData(), reducedNaiveTrainData, Constant.getBestWindowSize(ds.getName())/100d);
        result.setClassifyTime(System.currentTimeMillis() - startClassify);
        double[] correctionRateNaive = ClassificationEvaluator.evaluate(ds.getTestData(), MeasureMethod.DTW);
        result.setOriginalTrainSize(ds.getTrainData().size());
        result.setReducedTrainSize(reducedNaiveTrainData.size());
        result.setCorrectSize(((int) correctionRateNaive[0]));
        result.setTestSize(((int) correctionRateNaive[1]));
        result.setCorrectnessRate(correctionRateNaive[2]);
        result.setConsumedTime(System.currentTimeMillis() - start);
        result.setSpentTimeForReduction(spendTimeForReduction);
        result.setDataName(ds.getName());
        result.setMethod("KNN with NaiveRanking");
        return result;
    }

    private static ClassificationResult classifyKNNWithRHCTrainSet(DataSet ds) {
        long start = System.currentTimeMillis();
        ClassificationResult result = new ClassificationResult();
        long startRHC = System.currentTimeMillis();
        List<TimeSeries> reducedTrainData = RHC.reduce(ds.getTrainData());
        long spentTimeForReduction = System.currentTimeMillis() - startRHC;
        reducedTrainData.stream().forEach(e -> DataCache.getInstance().put(e));
        long startClassify = System.currentTimeMillis();
        KNNClassification.getInstance().classifyWithDTW(ds.getTestData(), reducedTrainData, Constant.getBestWindowSize(ds.getName())/100d);
        result.setClassifyTime(System.currentTimeMillis() - startClassify);
        double[] correctionRateRHC = ClassificationEvaluator.evaluate(ds.getTestData(), MeasureMethod.DTW);
        result.setConsumedTime(System.currentTimeMillis() - start);
        result.setCorrectSize(((int) correctionRateRHC[0]));
        result.setTestSize(((int) correctionRateRHC[1]));
        result.setCorrectnessRate(correctionRateRHC[2]);
        result.setOriginalTrainSize(ds.getTrainData().size());
        result.setReducedTrainSize(reducedTrainData.size());
        result.setSpentTimeForReduction(spentTimeForReduction);
        result.setDataName(ds.getName());
        result.setMethod("KNN with RHC");
        return result;
    }

    private static ClassificationResult classifyKNNWithOrginalTrainSet(DataSet ds) {
        long start = System.currentTimeMillis();
        ClassificationResult result = new ClassificationResult();
        long startClassify = System.currentTimeMillis();
        KNNClassification.getInstance().classifyWithDTW(ds.getTestData(), ds.getTrainData(), Constant.getBestWindowSize(ds.getName())/100d);
        result.setClassifyTime(System.currentTimeMillis() - startClassify);
        double[] correctionRate = ClassificationEvaluator.evaluate(ds.getTestData(), MeasureMethod.DTW);
        result.setConsumedTime(System.currentTimeMillis() - start);
        result.setCorrectSize(((int) correctionRate[0]));
        result.setTestSize(((int) correctionRate[1]));
        result.setCorrectnessRate(correctionRate[2]);
        result.setOriginalTrainSize(ds.getTrainData().size());
        result.setDataName(ds.getName());
        result.setMethod("KNN with original train set");
        return result;
    }

    private static ClassificationResult classifyKNNWithOrginalTrainSetWithWindowSize(DataSet ds, double windowSize) {
        long start = System.currentTimeMillis();
        ClassificationResult result = new ClassificationResult();
        long startClassify = System.currentTimeMillis();
        KNNClassification.getInstance().classifyWithDTW(ds.getTestData(), ds.getTrainData(), windowSize);
        result.setClassifyTime(System.currentTimeMillis() - startClassify);
        double[] correctionRate = ClassificationEvaluator.evaluate(ds.getTestData(), MeasureMethod.DTW);
        result.setConsumedTime(System.currentTimeMillis() - start);
        result.setCorrectSize(((int) correctionRate[0]));
        result.setTestSize(((int) correctionRate[1]));
        result.setCorrectnessRate(correctionRate[2]);
        result.setOriginalTrainSize(ds.getTrainData().size());
        result.setDataName(ds.getName());
        result.setMethod("KNN with original train set");
        return result;
    }

    private static void findTheBestWindowSize(List<String> dataNames) {
        ExecutorService es = Executors.newFixedThreadPool(16);
        List<CompletableFuture<Object[]>> futures = new ArrayList<>();
        for (String dataName : dataNames) {
            CompletableFuture<Object[]> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
                Object result[] = new Object[3];
                try {
                    List<TimeSeries> trainData = DataReader.readDataFile("data/" + dataName + "/" + dataName + "_TRAIN", dataName, InstanceId.Type.TRAIN);
                    List<TimeSeries> testData = DataReader.readDataFile("data/" + dataName + "/" + dataName + "_TEST", dataName, InstanceId.Type.TEST);
                    DataSet ds = new DataSet(dataName, trainData, testData);
                    System.out.println("-------------" + dataName + "(" + trainData.size() + "/" + testData.size() + ")-------------");

                    double bestWindowSize = 0;
                    int bestPercent = 0;
                    double bestCorrectnessRate = Double.MIN_VALUE;
                    for (int i = 1; i <= 100; i++) {
                        double windowSize = i / 100d;
                        ClassificationResult resultWithOriginalTrainSet = classifyKNNWithOrginalTrainSetWithWindowSize(ds, windowSize);
                        if (resultWithOriginalTrainSet.getCorrectnessRate() > bestCorrectnessRate) {
                            bestCorrectnessRate = resultWithOriginalTrainSet.getCorrectnessRate();
                            bestWindowSize = windowSize;
                            bestPercent = i;
                            System.out.println(dataName + ": " + bestCorrectnessRate+ ": " + bestWindowSize);
                        }
                        if(bestCorrectnessRate == 100d) {
                            break;
                        }
                    }
                    System.out.println("Final: " + dataName + ": " + bestCorrectnessRate+ ": " + bestWindowSize);
                    result[0] = dataName;
                    result[1] = bestCorrectnessRate;
                    result[2] = Integer.valueOf(bestPercent);
                    return  result;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new Object[]{"hello"};
            });
            futures.add(stringCompletableFuture);
        }
        SortedMap<String, Object[]> finalResults = new TreeMap<>();
        for (CompletableFuture<Object[]> future : futures) {
            try {
                Object[] result = future.get();
                finalResults.put(result[0].toString(), result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        es.shutdown();
        LatexUtil.buildBestWindowSizeTable(finalResults);
    }
}
