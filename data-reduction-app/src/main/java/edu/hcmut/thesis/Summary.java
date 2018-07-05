package edu.hcmut.thesis;

public class Summary {
    private String dataName;
    private int trainSize;
    private int testSize;
    private int reducedTrainSize;
    private ClassificationResult originalResult;
    private ClassificationResult rhcResult;
    private ClassificationResult naiveRankResult;

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public int getTrainSize() {
        return trainSize;
    }

    public void setTrainSize(int trainSize) {
        this.trainSize = trainSize;
    }

    public int getTestSize() {
        return testSize;
    }

    public void setTestSize(int testSize) {
        this.testSize = testSize;
    }

    public int getReducedTrainSize() {
        return reducedTrainSize;
    }

    public void setReducedTrainSize(int reducedTrainSize) {
        this.reducedTrainSize = reducedTrainSize;
    }

    public ClassificationResult getOriginalResult() {
        return originalResult;
    }

    public void setOriginalResult(ClassificationResult originalResult) {
        this.originalResult = originalResult;
    }

    public ClassificationResult getRhcResult() {
        return rhcResult;
    }

    public void setRhcResult(ClassificationResult rhcResult) {
        this.rhcResult = rhcResult;
    }

    public ClassificationResult getNaiveRankResult() {
        return naiveRankResult;
    }

    public void setNaiveRankResult(ClassificationResult naiveRankResult) {
        this.naiveRankResult = naiveRankResult;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "dataName='" + dataName + '\'' +
                ", trainSize=" + trainSize +
                ", testSize=" + testSize +
                ", reducedTrainSize=" + reducedTrainSize +
                ", originalResult=" + originalResult +
                ", rhcResult=" + rhcResult +
                ", naiveRankResult=" + naiveRankResult +
                '}';
    }
}
