package edu.hcmut.thesis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.hcmut.thesis.InstanceId.Type;

public class DataReader {

    private static final DataCache cache = DataCache.getInstance();

    public static List<TimeSeries> readDataFile(String filePath, String name, Type type) throws IOException {
        List<TimeSeries> result = new ArrayList<>();
        ClassLoader classLoader = DataReader.class.getClassLoader();
        File dataFile = new File(classLoader.getResource(filePath).getFile());
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            long id = 0;
            for (String line; (line = br.readLine()) != null; ) {
                TimeSeries ts = convertToTimeSeries(name, id, type, line);
                result.add(ts);
                cache.put(ts);
                id++;
            }
        }
        return result;
    }

    private static TimeSeries convertToTimeSeries(String name, long id, Type type, String data) {
        String[] split = data.split(",");
        String clazz = split[0];
        TimeSeries ts = new TimeSeries(new InstanceId(name, id, type), clazz);
        for (int i = 1; i < split.length; i++) {
            ts.addValue(Double.valueOf(split[i]));
        }
        return ts;
    }

}
