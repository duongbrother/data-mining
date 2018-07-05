package edu.hcmut.thesis.drhc;

import edu.hcmut.thesis.*;
import edu.hcmut.thesis.rhc.RHC;
import edu.hcmut.thesis.util.Constant;
import edu.hcmut.thesis.util.LatexUtil;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DRHCApp {

    public static void main(String[] args) throws Exception {
//        List<String> fullDataNames = Arrays.asList("50words", "Adiac", "ArrowHead", "Beef", "BeetleFly", "BirdChicken", "Car", "CBF", "ChlorineConcentration", "CinC_ECG_torso", "Coffee", "Computers", "Cricket_X", "Cricket_Y", "Cricket_Z", "DiatomSizeReduction", "DistalPhalanxOutlineAgeGroup", "DistalPhalanxOutlineCorrect", "DistalPhalanxTW", "Earthquakes", "ECG200", "ECG5000", "ECGFiveDays", "ElectricDevices", "FaceAll", "FaceFour", "FacesUCR", "FISH", "FordA", "FordB", "Gun_Point", "Ham", "HandOutlines", "Haptics", "Herring", "InlineSkate", "InsectWingbeatSound", "ItalyPowerDemand", "LargeKitchenAppliances", "Lighting2", "Lighting7", "MALLAT", "Meat", "MedicalImages", "MiddlePhalanxOutlineAgeGroup", "MiddlePhalanxOutlineCorrect", "MiddlePhalanxTW", "MoteStrain", "name.txt", "NonInvasiveFatalECG_Thorax1", "NonInvasiveFatalECG_Thorax2", "OliveOil", "OSULeaf", "PhalangesOutlinesCorrect", "Phoneme", "Plane", "ProximalPhalanxOutlineAgeGroup", "ProximalPhalanxOutlineCorrect", "ProximalPhalanxTW", "RefrigerationDevices", "ScreenType", "ShapeletSim", "ShapesAll", "SmallKitchenAppliances", "SonyAIBORobotSurface", "SonyAIBORobotSurfaceII", "StarLightCurves", "Strawberry", "SwedishLeaf", "Symbols", "synthetic_control", "ToeSegmentation1", "ToeSegmentation2", "Trace", "TwoLeadECG", "Two_Patterms", "UWaveGestureLibraryAll", "uWaveGestureLibrary_X", "uWaveGestureLibrary_Y", "uWaveGestureLibrary_Z", "wafer", "Wine", "WordsSynonyms", "Worms", "WormsTwoClass", "yoga");
//        List<String> dataNames = Arrays.asList("ArrowHead", "Beef", "BeetleFly", "BirdChicken", "Car", "CBF", "Coffee", "DiatomSizeReduction", "FISH", "Gun_Point", "Lighting2", "Lighting7", "Meat", "Plane", "Trace");
        List<String> dataNames = Arrays.asList("Trace", "Plane", "Haptics", "FISH", "FacesUCR", "Computers", "WordsSynonyms", "yoga", "Strawberry", "ScreenType", "Cricket_X", "SwedishLeaf", "ShapesAll", "StarLightCurves", "FordA");
//        List<String> dataNames = Arrays.asList("Trace", "Plane");//, "Haptics", "FISH", "FacesUCR", "Computers", "WordsSynonyms", "yoga", "Strawberry", "ScreenType", "Cricket_X", "SwedishLeaf", "ShapesAll", "StarLightCurves", "ElectricDevices");
//        Map<String, ClassificationResult[]> result = classifyWithAfterDRHC(dataNames);
//        LatexUtil.buildDRHCSummaryTable(result);
        printDataInfo(dataNames);
    }

    private static Map<String, ClassificationResult[]> classifyWithAfterDRHC(List<String> dataNames) {
        SortedMap<String, ClassificationResult[]> results = new TreeMap<>();
        ExecutorService es = Executors.newFixedThreadPool(16);

        List<CompletableFuture<ClassificationResult[]>> futures = new ArrayList<>();
        for (String dataName : dataNames) {
            CompletableFuture<ClassificationResult[]> uCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    ClassificationResult[] resultArray = new ClassificationResult[2];
                    List<TimeSeries> trainData = DataReader.readDataFile("data/" + dataName + "/" + dataName + "_TRAIN", dataName, InstanceId.Type.TRAIN);
                    List<TimeSeries> testData = DataReader.readDataFile("data/" + dataName + "/" + dataName + "_TEST", dataName, InstanceId.Type.TEST);
                    DataSet ds = new DataSet(dataName, trainData, testData);
                    resultArray[0] = classifyKNNWithOrginalTrainSet(ds);
                    resultArray[1] = classifyKNNWithDRHCTrainSet(ds);
                    return resultArray;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
            futures.add(uCompletableFuture);
        }
        for (CompletableFuture<ClassificationResult[]> future : futures) {
            try {
                ClassificationResult[] cr = future.get();
                System.out.println(cr[0]);
                System.out.println(cr[1]);
                results.put(cr[0].getDataName(), cr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private static ClassificationResult classifyKNNWithDRHCTrainSet(DataSet ds) {
        long start = System.currentTimeMillis();
        ClassificationResult result = new ClassificationResult();
        long startdRHC = System.currentTimeMillis();
        List<List<TimeSeries>> dataSegments = DataSegmentUtil.createBaseTrainsetAndDataSegments(ds.getTrainData(), 50, 10);
        List<TimeSeries> reducedTrainData = RHC.reduce(dataSegments.get(0));
        for (int i = 1; i < dataSegments.size(); i++) {
            reducedTrainData = DRHC.updateCS(reducedTrainData, dataSegments.get(i));
        }
        long spentTimeForReduction = System.currentTimeMillis() - startdRHC;
        reducedTrainData.stream().forEach(e -> DataCache.getInstance().put(e));
        long startClassify = System.currentTimeMillis();
        KNNClassification.getInstance().classifyWithDTW(ds.getTestData(), reducedTrainData, Constant.getBestWindowSize(ds.getName()) / 100d);
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
        result.setMethod("KNN with dRHC");
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

    private static void printDataInfo(List<String> dataNames) throws Exception {
        List<DataSet> dss = new ArrayList<>();
        for (String dataName : dataNames) {
            List<TimeSeries> trainData = DataReader.readDataFile("data/" + dataName + "/" + dataName + "_TRAIN", dataName, InstanceId.Type.TRAIN);
            List<TimeSeries> testData = DataReader.readDataFile("data/" + dataName + "/" + dataName + "_TEST", dataName, InstanceId.Type.TEST);
            DataSet ds = new DataSet(dataName, trainData, testData);
            dss.add(ds);

        }
        LatexUtil.buildDataSetInfoTable(dss);
    }
}
