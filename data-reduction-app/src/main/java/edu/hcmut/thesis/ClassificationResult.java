package edu.hcmut.thesis;

public class ClassificationResult {
    private String method;
    private String dataName;
    private double correctnessRate;
    private long consumedTime;
    private int testSize;
    private int originalTrainSize;
    private int correctSize;
    private int reducedTrainSize;
    private long spentTimeForReduction;
    private long classifyTime;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public double getCorrectnessRate() {
        return correctnessRate;
    }

    public void setCorrectnessRate(double correctnessRate) {
        this.correctnessRate = correctnessRate;
    }

    public long getConsumedTime() {
        return consumedTime;
    }

    public void setConsumedTime(long consumedTime) {
        this.consumedTime = consumedTime;
    }

    public int getTestSize() {
        return testSize;
    }

    public void setTestSize(int testSize) {
        this.testSize = testSize;
    }

    public int getOriginalTrainSize() {
        return originalTrainSize;
    }

    public void setOriginalTrainSize(int originalTrainSize) {
        this.originalTrainSize = originalTrainSize;
    }

    public int getCorrectSize() {
        return correctSize;
    }

    public void setCorrectSize(int correctSize) {
        this.correctSize = correctSize;
    }

    public int getReducedTrainSize() {
        return reducedTrainSize;
    }

    public void setReducedTrainSize(int reducedTrainSize) {
        this.reducedTrainSize = reducedTrainSize;
    }

    public long getSpentTimeForReduction() {
        return spentTimeForReduction;
    }

    public void setSpentTimeForReduction(long spentTimeForReduction) {
        this.spentTimeForReduction = spentTimeForReduction;
    }

    public long getClassifyTime() {
        return classifyTime;
    }

    public void setClassifyTime(long classifyTime) {
        this.classifyTime = classifyTime;
    }

    @Override
    public String toString() {
        return "ClassificationResult{" +
                "method='" + method + '\'' +
                ", dataName='" + dataName + '\'' +
                ", originalTrainSize=" + originalTrainSize +
                ", reducedTrainSize=" + reducedTrainSize +
                ", correctnessRate=" + correctnessRate +
                ", spentTimeForReduction=" + spentTimeForReduction +
                ", classifyTime=" + classifyTime +
                ", consumedTime=" + consumedTime +
                ", testSize=" + testSize +
                ", correctSize=" + correctSize +
                '}';
    }
}
