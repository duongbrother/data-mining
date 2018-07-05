package edu.hcmut.thesis;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataSet {

    private String name;

    private int nbClazz;

    private List<TimeSeries> trainData;

    private List<TimeSeries> testData;

    public DataSet(String name, List<TimeSeries> trainData, List<TimeSeries> testData) {
        this.name = name;
        this.trainData = trainData;
        this.testData = testData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNbClazz() {
        return nbClazz == 0 ? trainData.stream().map(e -> e.getClazz()).collect(Collectors.toSet()).size() : nbClazz;
    }

    public void setNbClazz(int nbClazz) {
        this.nbClazz = nbClazz;
    }

    public List<TimeSeries> getTrainData() {
        return trainData;
    }

    public void setTrainData(List<TimeSeries> trainData) {
        this.trainData = trainData;
    }

    public List<TimeSeries> getTestData() {
        return testData;
    }

    public void setTestData(List<TimeSeries> testData) {
        this.testData = testData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSet dataSet = (DataSet) o;
        return Objects.equals(name, dataSet.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "{" + toStringFields() + '}';
    }

    protected StringBuilder toStringFields() {
        StringBuilder sb = new StringBuilder();
        sb.append("name=").append(name);
        sb.append(",nbClazz=").append(nbClazz);
        sb.append(",trainData=").append(trainData);
        sb.append(",testData=").append(testData);
        return sb;
    }
}
