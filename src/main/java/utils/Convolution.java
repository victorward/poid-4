package utils;

import java.util.ArrayList;
import java.util.List;

public class Convolution {

    public static List<Double> computeConvolutedSignal(List<Double> xn, List<Double> hn) {
        List<Double> convolutedSignal = new ArrayList<>();

        for (int n = 0; n < (hn.size() + xn.size() - 1) * 2; n++) {
            double value = 0.0;
            for (int k = 0; k < hn.size(); k++) {
                if (n - k < xn.size() && n - k >= 0) {
                    value += hn.get(k) * xn.get(n - k);
                }
            }
            convolutedSignal.add(value);
        }

        return convolutedSignal;
    }
}
