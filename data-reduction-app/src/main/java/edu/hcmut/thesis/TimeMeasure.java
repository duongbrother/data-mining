package edu.hcmut.thesis;

import edu.hcmut.thesis.rhc.RHC;
import edu.hcmut.thesis.util.Constant;
import edu.hcmut.thesis.util.LatexUtil;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TimeMeasure {
    public static void main(String[] args) {
        List<String> dataNames = Arrays.asList("ArrowHead", "Beef", "BeetleFly", "BirdChicken", "Car", "CBF", "Coffee", "DiatomSizeReduction", "FISH", "Gun_Point", "Lighting2", "Lighting7", "Meat", "Plane", "Trace");
        classifyWithThreeDifferentStrategies(dataNames);
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

                ClassificationResult resultWithRHCTrainSet = classifyKNNWithRHCTrainSet(ds);
                subResult.add(resultWithRHCTrainSet);
                System.out.println(resultWithRHCTrainSet);

                ClassificationResult resultWithNaiveRankTrainSet = classifyKNNWithNaiveRankTrainSet(ds, new Double(60 * ds.getTrainData().size() / 100d).intValue());
                System.out.println(resultWithNaiveRankTrainSet);
                subResult.add(resultWithNaiveRankTrainSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
            results.put(dataName, subResult);
        }
        LatexUtil.buildClassifyTimeTable(results);
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
}
