package operations.filter;

import operations.WindowType;
import operations.soi.SoundSignal;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

abstract public class SOIFilter {

    protected double M = 0; // Długość okna
    protected double R = 0; // Przesunięcie
    protected double fc = 0; // Częstotliwość odcięcia
    protected double L = 0; // Długoćś odpowiedzi impulsowej
    protected double N = 0;
    protected int bits = 0;
    public static final double SAMPLING_FREQUENCY = 44100;
    protected List<SoundSignal> signalWindows = new ArrayList<>();
    protected List<Double> impulseResponse = new ArrayList<>();
    protected WindowType windowType = WindowType.SQUARE;
    protected List<Complex[]> soundSprectres = new ArrayList<>();
    protected Double[] outputSignal;
    protected List<Double[]> resultsOfFilterOperations = new ArrayList<>();

    protected int samplesCount;

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

    public List<SoundSignal> getSignalWindows() {
        return signalWindows;
    }

    public Double[] getOutputSignal() {
        return outputSignal;
    }

    public void setSignalWindows(Integer[] samples) {
        signalWindows.clear();
        this.samplesCount = samples.length;
        SoundSignal ss;
        System.out.println("samples.length " + samples.length);
        for (int windowIndex = 0; windowIndex < samples.length / M; windowIndex++) {
            System.out.println("windowIndex * M = " + windowIndex * M + "; (windowIndex * M) + M = " + (windowIndex * M) + M + "; M = " + M);
            ss = new SoundSignal((int) (windowIndex * M), (int) M, samples);
            for (int i = 0; i < N - M; i++) {
                ss.getSamples().add(0.0);
            }
//            System.out.println("SS size "+ss.getSamples().size());
            signalWindows.add(ss);
        }
    }

    public WindowType getWindowType() {
        return windowType;
    }

    public void setWindowType(WindowType windowType) {
        this.windowType = windowType;
    }

    public void computeLowPassFilterParameters() {
        impulseResponse.clear();
//        System.out.println("Start");
        double[] tab = new double[(int) L];
        for (int k = 0; k < L; k++) {
//            System.out.println(k + " ");
            if (k == (L - 1) / 2) {
                tab[k] = (2 * fc / SAMPLING_FREQUENCY);
            } else {
                tab[k] = computeStrangeSincFunction(k);
            }
            double window = windowType.getValue(k, M);
            tab[k] = tab[k] * window;
        }
        for (int i = 0; i < tab.length; i++) {
            impulseResponse.add(tab[i]);
        }
        for (int i = 0; i < N - L; i++) {
            impulseResponse.add(0.0);
        }
    }

    abstract public void computeFilter();

    protected static void addElems(int startingIndex, Double[] src, Double[] dest) {
        for (int i = 0; i < src.length; i++) {
            if (i + startingIndex < dest.length) {
//                System.out.println( (i+startingIndex) + " " + src[i]);
                dest[i + startingIndex] += src[i];
            }
        }
    }

    protected double computeStrangeSincFunction(double k) {
        double up = 0.0, down = 0.0;
        double PI = Math.PI;
        double x = k - ((L - 1) / 2.0);
        up = Math.sin(((2.0 * PI * fc) / (SAMPLING_FREQUENCY)) * x);
        down = PI * x;
        return up / down;
    }

    protected double computeStrangeSincFunction(double k, double fc) {
        double up = 0.0, down = 0.0;
        double PI = Math.PI;
        double x = k - ((L - 1) / 2.0);
        up = Math.sin(((2.0 * PI * fc) / (SAMPLING_FREQUENCY)) * x);
        down = PI * x;
        return up / down;
    }
}
