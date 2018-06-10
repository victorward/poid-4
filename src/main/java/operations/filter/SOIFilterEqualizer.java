package operations.filter;

import operations.WindowType;
import operations.equalizer.Equalizer;
import operations.soi.SoundSignal;
import org.apache.commons.math3.complex.Complex;
import utils.Fourier;
import utils.chart.ChartDrawer;

import java.util.ArrayList;
import java.util.List;

public class SOIFilterEqualizer extends SOIFilter {

    private Equalizer equalizer;
    private Complex[] responseForEqualizer;

    public SOIFilterEqualizer(Equalizer equalizer) {
        this.equalizer = equalizer;
    }

    public Equalizer getEqualizer() {
        return equalizer;
    }

    @Override
    public void computeFilter() {
        Double[] eqResp = new Double[(int) N];
        outputSignal = new Double[samplesCount];
        for (int i = 0; i < outputSignal.length; i++) {
            outputSignal[i] = 0.0;
        }
        Complex[] signalTab;
        Complex[] resultOfChangingAmplitude;
        resultsOfFilterOperations.clear();
        double frequencyResolution = SAMPLING_FREQUENCY / N;
        List<SoundSignal> signals = new ArrayList<>(signalWindows);

        for (int i = 0; i < eqResp.length; i++) {
            eqResp[i] = 0.0;
        }

        for (int equalizerSlide = 0; equalizerSlide < equalizer.getSliders().size(); equalizerSlide++) {
            int leftSide = (int) ((equalizer.getSliders().get(equalizerSlide).getLeftSideOfFrequencies() * N) / SAMPLING_FREQUENCY);
            int rightSide = (int) ((equalizer.getSliders().get(equalizerSlide).getRightSideOfFrequencies() * N) / SAMPLING_FREQUENCY);
            double edge = 0;
            if (equalizer.getSliders().get(equalizerSlide).getEdge() >= 1) {
                edge = equalizer.getSliders().get(equalizerSlide).getEdge();
            } else {
                edge = 1.0 / Math.abs(equalizer.getSliders().get(equalizerSlide).getEdge());
            }
//                    System.out.println(leftSide + " " + rightSide + " " + edge);
            for (int spectrumIndex = leftSide; spectrumIndex < rightSide; spectrumIndex++) {
                eqResp[spectrumIndex] = new Double(edge);
                eqResp[eqResp.length - spectrumIndex - 1] = new Double(edge);
            }
        }
        ChartDrawer.drawChart(eqResp, "Equalizer");
        for (int windowIndex = 0; windowIndex < signalWindows.size(); windowIndex++) {
            signalTab = new Complex[signals.get(windowIndex).getSamples().size()];
//            System.out.println(signalTab.length);
            for (int sampleIndex = 0; sampleIndex < signalTab.length; sampleIndex++) {
                signalTab[sampleIndex] = new Complex(signals.get(windowIndex).getSamples().get(sampleIndex), 0.0);
            }
            signalTab = Fourier.computeFastFourier(signalTab, bits);

            resultOfChangingAmplitude = new Complex[signalTab.length];
            for (int i = 0; i < resultOfChangingAmplitude.length; i++) {
                resultOfChangingAmplitude[i] = new Complex(0, 0);
            }
            for (int i = 0; i < resultOfChangingAmplitude.length; i++) {
                resultOfChangingAmplitude[i] = signalTab[i].multiply(eqResp[i]);
            }
            if (windowIndex == signalWindows.size() / 2) {
                ChartDrawer.drawChart(signalTab, "Signal before");
                ChartDrawer.drawChart(resultOfChangingAmplitude, "Window spectrum after");
            }
            resultOfChangingAmplitude = Fourier.computeInverseFastFourier(resultOfChangingAmplitude, bits);
            if (windowIndex == signalWindows.size() / 2) {
                ChartDrawer.drawFromChart(resultOfChangingAmplitude, "Window after inverse");
            }
            Double[] d = new Double[resultOfChangingAmplitude.length];
            for (int i = 0; i < d.length; i++) {
                d[i] = resultOfChangingAmplitude[i].getReal();
            }
            resultsOfFilterOperations.add(d);
        }
        System.out.println("Końcowka dodaję");
        for (int i = 0; i < resultsOfFilterOperations.size(); i++) {
            System.out.println((i + 1) + " Elem " + (i * R));
            addElems((int) (i * R), resultsOfFilterOperations.get(i), outputSignal);
        }
    }

    @Override
    public void setWindowType(WindowType windowType
    ) {

    }

    @Override
    public void setFc(double fc
    ) {

    }

}
