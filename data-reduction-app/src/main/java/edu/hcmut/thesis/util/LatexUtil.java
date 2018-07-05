package edu.hcmut.thesis.util;

import edu.hcmut.thesis.ClassificationResult;
import edu.hcmut.thesis.DataSet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class LatexUtil {

     public static void buildSummaryTable(Map<String, List<ClassificationResult>> result) {
        StringBuilder sb = new StringBuilder();
        BigDecimal sumReductionRate = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        BigDecimal sumOriginalRate = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        BigDecimal sumRhcRate = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        BigDecimal sumNaiveRate = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        for (Map.Entry<String, List<ClassificationResult>> stringListEntry : result.entrySet()) {
            List<ClassificationResult> cresult = stringListEntry.getValue();
            ClassificationResult original = cresult.get(0);
            ClassificationResult rhc = cresult.get(1);
            ClassificationResult naive = cresult.get(2);

            sb.append(stringListEntry.getKey()).append(" & ");
            sb.append(original.getOriginalTrainSize()).append(" & ");
            sb.append(rhc.getReducedTrainSize()).append(" & ");
            BigDecimal reductionRate = new BigDecimal((double) rhc.getReducedTrainSize() * 100 / original.getOriginalTrainSize()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sumReductionRate = sumReductionRate.add(reductionRate);
            sb.append(reductionRate).append(" & ");
            BigDecimal originalRate = new BigDecimal(original.getCorrectnessRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sumOriginalRate = sumOriginalRate.add(originalRate);
            sb.append(originalRate).append(" & ");
            BigDecimal rhcRate = new BigDecimal(rhc.getCorrectnessRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sumRhcRate = sumRhcRate.add(rhcRate);
            BigDecimal naiveRate = new BigDecimal(naive.getCorrectnessRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sb.append(rhcRate.compareTo(naiveRate) > 0 ? "\\textbf{" + rhcRate + "}" : rhcRate).append(" & ");
            sb.append(naiveRate.compareTo(rhcRate) > 0 ? "\\textbf{" + naiveRate + "}" : naiveRate);
            sumNaiveRate = sumNaiveRate.add(naiveRate);
            sb.append("\\\\ \\hline");
            sb.append("\n");
        }
        /*
        \multicolumn{3}{|c|}{\textbf{Trung bình}} & \textbf{24.9} & \textbf{80.4} & \textbf{74.6} & \textbf{62.2}\\ \hline
         */
        sb.append("\\multicolumn{3}{|c|}{\\textbf{Trung bình}}").append(" & ");
        sb.append("\\textbf{").append(sumReductionRate.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}").append( " & ");
        sb.append("\\textbf{").append(sumOriginalRate.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}").append( " & ");
        sb.append("\\textbf{").append(sumRhcRate.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}").append( " & ");
        sb.append("\\textbf{").append(sumNaiveRate.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}");
        sb.append("\\\\ \\hline");
        System.out.println(sb.toString());
    }

    public static void buildDRHCSummaryTableSingle(Map<String, ClassificationResult>result) {
        StringBuilder sb = new StringBuilder();
        BigDecimal sumReductionRate = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        long sumReductionTime = 0;
        BigDecimal sumRhcRate = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);

        for (Map.Entry<String, ClassificationResult> stringListEntry : result.entrySet()) {
            ClassificationResult drhc = stringListEntry.getValue();
            sb.append(stringListEntry.getKey()).append(" & ");
            sb.append(drhc.getOriginalTrainSize()).append(" & ");
            sb.append(drhc.getReducedTrainSize()).append(" & ");
            BigDecimal reductionRate = new BigDecimal((double) drhc.getReducedTrainSize() * 100 / drhc.getOriginalTrainSize()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sumReductionRate = sumReductionRate.add(reductionRate);
            sb.append(reductionRate).append(" & ");
            sb.append(drhc.getSpentTimeForReduction()).append(" & ");
            BigDecimal rhcRate = new BigDecimal(drhc.getCorrectnessRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sb.append(rhcRate);
            sb.append("\\\\ \\hline");
            sb.append("\n");
            sumRhcRate = sumRhcRate.add(rhcRate);
            sumReductionTime += drhc.getSpentTimeForReduction();
        }
        /*
        \multicolumn{3}{|c|}{\textbf{Trung bình}} & \textbf{24.9} & \textbf{80.4} & \textbf{74.6} & \textbf{62.2}\\ \hline
         */
        sb.append("\\multicolumn{3}{|c|}{\\textbf{Trung bình}}").append(" & ");
        sb.append("\\textbf{").append(sumReductionRate.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}").append( " & ");
        sb.append("\\textbf{").append(sumReductionTime/result.size()).append("}").append( " & ");
        sb.append("\\textbf{").append(sumRhcRate.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}");
        sb.append("\\\\ \\hline");
        System.out.println(sb.toString());
    }

    public static void buildDRHCSummaryTable(Map<String, ClassificationResult[]>result) {
        StringBuilder sb = new StringBuilder();
        BigDecimal sumReductionRate = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        long sumReductionTime = 0;
        BigDecimal sumOriginalRate = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        BigDecimal sumRhcRate = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);

        for (Map.Entry<String, ClassificationResult[]> stringListEntry : result.entrySet()) {

            ClassificationResult original = stringListEntry.getValue()[0];
            ClassificationResult drhc = stringListEntry.getValue()[1];
            sb.append(stringListEntry.getKey()).append(" & ");
            sb.append(drhc.getOriginalTrainSize()).append(" & ");
            sb.append(drhc.getReducedTrainSize()).append(" & ");
            BigDecimal reductionRate = new BigDecimal((double) drhc.getReducedTrainSize() * 100 / drhc.getOriginalTrainSize()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sumReductionRate = sumReductionRate.add(reductionRate);
            sb.append(reductionRate).append(" & ");
            sb.append(drhc.getSpentTimeForReduction()).append(" & ");
            BigDecimal drhcRate = new BigDecimal(drhc.getCorrectnessRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
            BigDecimal originalRate = new BigDecimal(original.getCorrectnessRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sb.append(drhcRate.compareTo(originalRate) > 0 ? "\\textbf{" + drhcRate + "}" : drhcRate).append(" & ");
            sb.append(originalRate.compareTo(drhcRate) > 0 ? "\\textbf{" + originalRate + "}" : originalRate);

            sb.append("\\\\ \\hline");
            sb.append("\n");
            sumReductionTime += drhc.getSpentTimeForReduction();
            sumRhcRate = sumRhcRate.add(drhcRate);
            sumOriginalRate = sumOriginalRate.add(originalRate);
        }
        /*
        \multicolumn{3}{|c|}{\textbf{Trung bình}} & \textbf{24.9} & \textbf{80.4} & \textbf{74.6} & \textbf{62.2}\\ \hline
         */
        sb.append("\\multicolumn{3}{|c|}{\\textbf{Trung bình}}").append(" & ");
        sb.append("\\textbf{").append(sumReductionRate.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}").append( " & ");
        sb.append("\\textbf{").append(sumReductionTime/result.size()).append("}").append( " & ");
        sb.append("\\textbf{").append(sumRhcRate.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}").append(" & ");
        sb.append("\\textbf{").append(sumOriginalRate.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}");
        sb.append("\\\\ \\hline");
        System.out.println(sb.toString());
    }

    public static void buildTimeTable(Map<String, List<ClassificationResult>> result) {
        StringBuilder sb = new StringBuilder();
        BigDecimal sumReductionRate = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        long sumRhcTime = 0;
        long sumNaiveTime = 0;
        for (Map.Entry<String, List<ClassificationResult>> stringListEntry : result.entrySet()) {
            List<ClassificationResult> cresult = stringListEntry.getValue();
            ClassificationResult original = cresult.get(0);
            ClassificationResult rhc = cresult.get(1);
            ClassificationResult naive = cresult.get(2);

            sb.append(stringListEntry.getKey()).append(" & ");
            sb.append(original.getOriginalTrainSize()).append(" & ");
            sb.append(rhc.getReducedTrainSize()).append(" & ");
            BigDecimal reductionRate = new BigDecimal((double) rhc.getReducedTrainSize() * 100 / original.getOriginalTrainSize()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sumReductionRate = sumReductionRate.add(reductionRate);
            sb.append(reductionRate).append(" & ");

            boolean isRhcLower = rhc.getSpentTimeForReduction() < naive.getSpentTimeForReduction();
            boolean isNaiveLower = naive.getSpentTimeForReduction() < rhc.getSpentTimeForReduction();
            sb.append(isRhcLower ? "\\textbf{" + rhc.getSpentTimeForReduction() + "}" : rhc.getSpentTimeForReduction()).append(" & ");
            sb.append(isNaiveLower ? "\\textbf{" + naive.getSpentTimeForReduction() + "}" : naive.getSpentTimeForReduction());

            sumRhcTime += rhc.getSpentTimeForReduction();
            sumNaiveTime += naive.getSpentTimeForReduction();
            sb.append("\\\\ \\hline");
            sb.append("\n");
        }
        /*
        \multicolumn{3}{|c|}{\textbf{Trung bình}} & \textbf{24.9} & \textbf{80.4} & \textbf{74.6} & \textbf{62.2}\\ \hline
         */
        sb.append("\\multicolumn{3}{|c|}{\\textbf{Trung bình}}").append(" & ");
        sb.append("\\textbf{").append(sumReductionRate.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}").append( " & ");
        sb.append("\\textbf{").append(sumRhcTime / result.size()).append("}").append( " & ");
        sb.append("\\textbf{").append(sumNaiveTime / result.size()).append("}");
        sb.append("\\\\ \\hline");
        System.out.println(sb.toString());
    }

    public static void buildBestWindowSizeTable(Map<String, Object[]> resultMap) {
        for (Map.Entry<String, Object[]> stringEntry : resultMap.entrySet()) {
            Object[] result = stringEntry.getValue();
            StringBuilder sb = new StringBuilder();
            sb.append("\\rownumber ").append(" & ");
            sb.append(result[0]).append(" & ");
            sb.append(new BigDecimal(Double.valueOf(result[1].toString())).setScale(1, BigDecimal.ROUND_HALF_UP)).append("& ");
            sb.append(result[2]).append("\\\\ \\hline");
            sb.append("\n");
            System.out.println(sb.toString());
        }
    }

    public static void buildNaiveRankSummaryTable(Map<String, List<ClassificationResult>> result) {
        StringBuilder sb = new StringBuilder();

        BigDecimal sumThree = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        BigDecimal sumFour = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        BigDecimal sumFive = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        BigDecimal sumSix = new BigDecimal(0).setScale(1, BigDecimal.ROUND_HALF_UP);
        for (Map.Entry<String, List<ClassificationResult>> stringListEntry : result.entrySet()) {
            List<ClassificationResult> cresult = stringListEntry.getValue();
            ClassificationResult three = cresult.get(0);
            ClassificationResult four = cresult.get(1);
            ClassificationResult five = cresult.get(2);
            ClassificationResult six = cresult.get(3);

            sb.append(stringListEntry.getKey()).append(" & ");
            BigDecimal threeRate = new BigDecimal(three.getCorrectnessRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sb.append(threeRate).append(" & ");
            BigDecimal fourRate = new BigDecimal(four.getCorrectnessRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sb.append(fourRate).append(" & ");
            BigDecimal fiveRate = new BigDecimal(five.getCorrectnessRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sb.append(fiveRate).append(" & ");
            BigDecimal sixRate = new BigDecimal(six.getCorrectnessRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
            sb.append(sixRate);
            sb.append("\\\\ \\hline");
            sb.append("\n");

            sumThree = sumThree.add(threeRate);
            sumFour = sumFour.add(fourRate);
            sumFive = sumFive.add(fiveRate);
            sumSix = sumSix.add(sixRate);
        }
        sb.append("\\textbf{Trung bình}").append(" & ");
        sb.append("\\textbf{").append(sumThree.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}").append( " & ");
        sb.append("\\textbf{").append(sumFour.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}").append( " & ");
        sb.append("\\textbf{").append(sumFive.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}").append( " & ");
        sb.append("\\textbf{").append(sumSix.divide(new BigDecimal(result.size()), 1, BigDecimal.ROUND_HALF_UP)).append("}");
        sb.append("\\\\ \\hline");
        System.out.println(sb.toString());
    }

    public static void buildDataSetInfoTable(List<DataSet> dss) {
         StringBuilder sb = new StringBuilder();
        for (DataSet dataSet : dss) {
            sb.append("\\rownumber").append(" & ");
            sb.append(dataSet.getName()).append(" & ");
            sb.append(dataSet.getTrainData().iterator().next().getValues().size()).append(" & ");
            sb.append(dataSet.getNbClazz()).append(" & ");
            sb.append(dataSet.getTrainData().size()).append(" & ");
            sb.append(dataSet.getTestData().size());
            sb.append("\\\\ \\hline");
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public static void buildClassifyTimeTable(Map<String, List<ClassificationResult>> result) {
        StringBuilder sb = new StringBuilder();
        long sumRhcTime = 0;
        long sumNaiveTime = 0;
        for (Map.Entry<String, List<ClassificationResult>> stringListEntry : result.entrySet()) {
            List<ClassificationResult> cresult = stringListEntry.getValue();
            ClassificationResult rhc = cresult.get(0);
            ClassificationResult naive = cresult.get(1);

            sb.append(stringListEntry.getKey()).append(" & ");
            sb.append(rhc.getClassifyTime()).append(" & ");
            sb.append(naive.getClassifyTime());
            sumRhcTime += rhc.getClassifyTime();
            sumNaiveTime += naive.getClassifyTime();
            sb.append("\\\\ \\hline");
            sb.append("\n");
        }
        sb.append("\\textbf{Trung bình}").append(" & ");
        sb.append("\\textbf{").append(sumRhcTime/result.size()).append("}").append( " & ");
        sb.append("\\textbf{").append(sumNaiveTime/result.size()).append("}");
        sb.append("\\\\ \\hline");
        System.out.println(sb.toString());
    }
}
