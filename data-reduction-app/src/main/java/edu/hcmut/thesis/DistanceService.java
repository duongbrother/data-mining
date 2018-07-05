package edu.hcmut.thesis;

import weka.core.matrix.Matrix;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class DistanceService {

    private static final double DWT_WINDOW_SIZE_PERCENT = 0.04d;

    private static final DistanceService instance = new DistanceService();

    private DistanceService() {}

    public static DistanceService getInstance() {
        return instance;
    }

    private static BigDecimal getDistance(List<Double> x, List<Double> y, MeasureMethod method) {
        return MeasureMethod.DTW == method ? getDTWDistance(x, y, Double.valueOf(Math.min(x.size(), y.size()) * DWT_WINDOW_SIZE_PERCENT).intValue()) : getEuclidDistance(x, y);
    }

    public static BigDecimal getDistance(TimeSeries x, TimeSeries y, MeasureMethod method) {
        return getDistance(x.getValues(), y.getValues(), method);
    }

    public static BigDecimal getDTWDistanceWithWindowSize(List<Double> x, List<Double> y, double windowSize) {
        return getDTWDistance(x, y, Double.valueOf(Math.min(x.size(), y.size()) * windowSize).intValue());
    }

    public static BigDecimal getDTWDistance(List<Double> x, List<Double> y, int r) {
        int row = x.size();
        int col = y.size();

        if (r == 0 || r > x.size()) r = x.size();

        double[][] matrix = new double[row][col];

//        matrix[0][0] = (x.get(0) - y.get(0)) * (x.get(0) - y.get(0));
        matrix[0][0] = Math.abs(x.get(0) - y.get(0));
        //Tính  các giá trị cho cột đầu tiên
        for (int i = 1; i < row; i++) {
//            matrix[i][0] = matrix[i - 1][0] + (x.get(i) - y.get(0)) * (x.get(i) - y.get(0));
            matrix[i][0] = matrix[i - 1][0] + Math.abs((x.get(i) - y.get(0)));
        }
        //Tính các giá trị cho dòng đầu tiên
        for (int i = 1; i < col; i++) {
//            matrix[0][i] = matrix[0][i - 1] + (x.get(0) - y.get(i)) * (x.get(0) - y.get(i));
            matrix[0][i] = matrix[0][i - 1] + Math.abs(x.get(0) - y.get(i));
        }
        boolean isOverflow = true;
        for (int i = 1; i < row; i++) {
            isOverflow = true;
            for (int j = 1; j < col; j++) {
                if (Math.abs(i - j) <= r) {
                    double v = Math.min(Math.min(matrix[i - 1][j], matrix[i][j - 1]), matrix[i - 1][j - 1]);
//                    matrix[i][j] = v + (x.get(i) - y.get(j)) * (x.get(i) - y.get(j));
                    matrix[i][j] = v + Math.abs(x.get(i) - y.get(j));
                    isOverflow = false;
                } else {
                    matrix[i][j] = Double.MAX_VALUE;
                }
            }
            if (isOverflow) {
                break;
            }
        }
//        Matrix mt = new Matrix(matrix);
//        printMatrix(mt);

        if (isOverflow || matrix[row - 1][col - 1] == Double.MAX_VALUE) {
            return BigDecimal.valueOf(Double.MAX_VALUE);
        } else {
            return BigDecimal.valueOf(Math.sqrt(matrix[row - 1][col - 1]));
        }
    }

    private static BigDecimal getEuclidDistance(List<Double> x, List<Double> y) {
        int length = Integer.min(x.size(), y.size());
        double tmp = 0;
        for (int i = 0; i < length; i++) {
            tmp += (x.get(i) - y.get(i)) * (x.get(i) - y.get(i));
        }
        return BigDecimal.valueOf(Math.sqrt(tmp));
    }

    public static void main(String[] args) {
//        List<Double> A = Arrays.asList(1d, 3d, 4d, 9d, 8d, 2d, 1d, 5d, 7d, 3d);
//        List<Double> B = Arrays.asList(1d, 6d, 2d, 3d, 0d, 9d, 4d, 3d, 6d, 3d);
//        System.out.println(getDTWDistance(B, A, 2));

        List<Double> A = Arrays.asList(0.23038, 0.49097, 0.35883, -0.23097, 0.090225, -0.50147, -0.33933, 0.066836, 0.23295, 0.028326, 0.2317, 0.016587, 0.14365, 0.28534, 0.071383, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, -0.16328, 0.096867, -0.60353, 0.25774, -0.14132, -0.29348, -0.43529, 0.4091, 0.87898, 0.48806, 0.051819, 0.38952, 0.30774, 0.50258, 0.054011, -0.23489, 0.16741, 0.52882, -0.26022, 0.23291, 0.51615, 0.086857, -0.14513, -0.91454, -0.14174, 0.38032, -0.38236, -0.0032941, 0.44991, 0.12915, -0.3133, -0.72994, 0.11114, 0.32361, -0.24927, 0.47836, -0.2672, -0.10918, 0.57008, -0.2712, 0.46439, -0.3136, -0.52394, -0.15864, 0.031504, -0.090676, -0.35231, 0.16837, 0.024811, -0.26357, -0.19797, 0.22408, 0.25016, -0.35094, -0.14001, -0.44532, 0.43899, -0.74483, -0.0735, -0.25038, 0.30119, -0.0067174, -0.023324, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, 1.6601, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, -1.6801, 0.10504, -0.26484, 0.30659, -0.075228, -0.22219, 0.21074, -0.25543, -0.27894);
        List<Double> B = Arrays.asList(0.00076522, -0.34268, 0.18873, -0.47936, -0.28193, 0.35193, -0.36033, 0.12182, -0.2855, 0.085623, -0.22798, -0.18773, 0.70146, 0.55897, -0.12367, -0.0097016, -0.74927, 0.073456, -0.061191, -0.38062, -0.02389, 0.13922, 0.24901, -0.10259, -0.19548, 0.17119, -0.027331, -0.11469, 0.4838, -0.40902, -0.15752, -0.60662, 0.27475, -0.58004, 0.007065, 0.10997, -0.045401, -0.17785, -0.13033, -0.19091, 0.065102, 0.63648, -0.11926, 0.30315, 0.50862, 0.14978, 0.12519, 0.73146, 0.18388, 0.34318, 0.12592, 0.23421, -0.05094, -0.089539, 0.51682, -0.0035485, 0.055431, -0.16603, -0.13325, -0.29474, 0.086777, -0.28911, -0.11785, -0.39267, -0.12362, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, -0.095597, -0.080362, -0.43231, -0.020249, -0.17343, -0.11621, -0.029407, 0.4164, 0.35533, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, -1.6999, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, 1.6997, -0.12937, -0.41724, 0.23782, 0.14847, -0.22147, -0.09035, 0.60616, -0.58922, -0.18748, -0.18987, -0.6427, -0.12149, 0.5259);
        long start = System.nanoTime();
        System.out.println(getDistance(A, B, MeasureMethod.DTW));
        System.out.println("Spent time: " + (System.nanoTime() - start) + " ns");
    }

    private static String printMatrix(Matrix matrix) {
        StringBuffer text = new StringBuffer();
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                text.append(" ").append(matrix.get(i, j) == Double.MAX_VALUE ? "xx" : String.format("%.4f", matrix.get(i, j)));
            }
            text.append("\n");
        }
        System.out.println(text.toString());
        return text.toString();
//       return matrix.toString();
    }
}
