package operations.filter;

import java.util.ArrayList;
import java.util.List;

public class SOIFilterConvolution extends SOIFilter {
    @Override
    public void setSignalWindows(double[] samples) {
        this.samplesCount = samples.length;
        this.signal = samples;
    }

    @Override
    public void computeLowPassFilterParameters() {
        impulseResponse.clear();
        double[] tab = new double[(int) L];
        tab = baseFilter(tab);
        for (double aTab : tab) {
            impulseResponse.add(aTab);
        }
    }

    @Override
    public void computeFilter() {
        double[] expandedSignal = expand();
        outputSignal = new Double[samplesCount];
        List<Double> convolutedElements = convolve(expandedSignal, impulseResponse);
        outputSignal = convolutedElements.toArray(new Double[convolutedElements.size()]);
    }

    private List<Double> convolve(double[] expandedSignal, List<Double> filter) {
        List<Double> convolutedSignal = new ArrayList<>(samplesCount);
//        double[] filteredSignal = new double[samplesCount];
        for (int i = 0, j = (int) (L - 1); i < samplesCount; i++, j++) {
            double value = 0.0;
            for (int k = 0; k < L; k++) {
                value += (expandedSignal[j - k] * filter.get(k));
            }
            convolutedSignal.add(value);
        }

        return convolutedSignal;
    }

    private double[] expand() {
        double[] expandedSignal = new double[(int) (samplesCount + L - 1)];
        prependValues(expandedSignal);
        appendSignal(expandedSignal);
        return expandedSignal;
    }

    private void prependValues(double[] expandedSignal) {
        for (int i = 0, j = (int) (L - 1); i < L - 1; i++, j--) {
            expandedSignal[i] = signal[j];
        }
    }

    private void appendSignal(double[] expandedSignal) {
        for (int i = (int) (L - 1), j = 0; i < expandedSignal.length; i++, j++) {
            expandedSignal[i] = signal[j];
        }
    }
}
