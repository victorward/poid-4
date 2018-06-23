package operations.filter;

import operations.WindowType;
import operations.soi.SoundSignal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.sin;

abstract public class SOIFilter {

    double M = 0; // Długość okna
    double R = 0; // Przesunięcie
    private double fc = 0; // Częstotliwość odcięcia
    double L = 0; // Długoćś odpowiedzi impulsowej
    double N = 0;
    int bits = 0;
    public static final double SAMPLING_FREQUENCY = 44100;
    List<Double> impulseResponse = new ArrayList<>();
    WindowType windowType = WindowType.SQUARE;
    Double[] outputSignal;
    double[] signal;

    List<SoundSignal> signalWindows = new ArrayList<>();
    List<Double[]> resultsOfFilterOperations = new ArrayList<>();

    int samplesCount;
    ArrayList<double[]> windows;

    public double getN() {
        return N;
    }

    public void setN(int bits) {
        this.bits = bits;
        this.N = Math.pow(2, bits);
    }

    public double getM() {
        return M;
    }

    public void setM(double M) {
        this.M = M;
    }

    public double getR() {
        return R;
    }

    public void setR(double R) {
        this.R = R;
    }

    public double getFc() {
        return fc;
    }

    public void setFc(double fc) {
        this.fc = fc;
    }

    public double getL() {
        return L;
    }

    public void setL(double L) {
        this.L = L;
    }

    public Double[] getOutputSignal() {
        return outputSignal;
    }

    public void setSignalWindows(double[] samples) {
        this.samplesCount = samples.length;
        this.signal = samples;
        windows = new ArrayList<>();
    }

    public WindowType getWindowType() {
        return windowType;
    }

    public void setWindowType(WindowType windowType) {
        this.windowType = windowType;
    }

    public void computeLowPassFilterParameters() {
        impulseResponse.clear();
        double[] tab = new double[(int) L];
        tab = baseFilter(tab);
        tab = fillWithZeros(tab);
        for (double aTab : tab) {
            impulseResponse.add(aTab);
        }
    }

    double[] fillWithZeros(double[] array) {
        int desiredSize = findDesiredSize();
        return expandArray(array, desiredSize);
    }

    private double[] expandArray(double[] array, int desiredSize) {
        double[] expandedArray = new double[desiredSize];
        int i = 0;
        for (; i < array.length; ++i) {
            expandedArray[i] = array[i];
        }
        for (; i < desiredSize; ++i) {
            expandedArray[i] = 0;
        }
        return expandedArray;
    }

    private int findDesiredSize() {
        int desiredSize = 1;
        while (desiredSize < M + L - 1) {
            desiredSize *= 2;
        }
        return desiredSize;
    }

    double[] baseFilter(double[] tab) {
//        double halfFilterLength = (L - 1) / 2;
        int halfFilterLength = (int) (L / 2);
        double frequenciesRatio = 2.0 * fc / SAMPLING_FREQUENCY;
        double pi = Math.PI;
        for (int k = 0; k < L; k++) {
            if (k == halfFilterLength) {
                tab[k] = frequenciesRatio;
            } else {
                double difference = pi * (k - halfFilterLength);
                tab[k] = sin(frequenciesRatio * difference) / difference;
            }
            double window = windowType.getValue(k, L);
            tab[k] = tab[k] * window;
        }

        return normalize(tab);
    }

    private double[] normalize(double[] filter) {
        double[] normalizedFilter = new double[(int) L];
        double sum = Arrays.stream(filter).sum();
        double multiplier = 1.0 / sum;
        for (int i = 0; i < L; i++) {
            normalizedFilter[i] = filter[i] * multiplier;
        }
        return normalizedFilter;
    }

    abstract public void computeFilter();

    static void addElems(int startingIndex, Double[] src, Double[] dest) {
        for (int i = 0; i < src.length; i++) {
            if (i + startingIndex < dest.length) {
//                System.out.println( (i+startingIndex) + " " + src[i]);
                dest[i + startingIndex] += src[i];
            }
        }
    }
}
