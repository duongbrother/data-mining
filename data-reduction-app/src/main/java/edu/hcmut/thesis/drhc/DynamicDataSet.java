package edu.hcmut.thesis.drhc;

import edu.hcmut.thesis.DataSet;
import edu.hcmut.thesis.TimeSeries;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataSet extends DataSet {

    private List<List<TimeSeries>> dataSegments;

    public DynamicDataSet(String name, List<TimeSeries> trainData, List<List<TimeSeries>> dataSegments, List<TimeSeries> testData) {
        super(name, trainData, testData);
        this.dataSegments = dataSegments;
    }

    public List<List<TimeSeries>> getDataSegments() {
        return dataSegments;
    }

    public void setDataSegments(List<List<TimeSeries>> dataSegments) {
        this.dataSegments = dataSegments;
    }

    @Override
    public String toString() {
        return "{" + toStringFields() + '}';
    }

    @Override
    protected StringBuilder toStringFields() {
        StringBuilder sb = super.toStringFields();
        sb.append(",dataSegments").append(dataSegments);
        return sb;
    }
}
