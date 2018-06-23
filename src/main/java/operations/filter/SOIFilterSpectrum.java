package operations.filter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.complex.Complex;
import utils.FrequencyTransform;

import java.util.Arrays;
import java.util.List;

public class SOIFilterSpectrum extends SOIFilter {


    @Override
    public void computeFilter() {
        System.out.println("Samples count " + samplesCount);
        outputSignal = new Double[samplesCount];
        for (int i = 0; i < outputSignal.length; i++) {
            outputSignal[i] = 0.0;
        }
        resultsOfFilterOperations.clear();
        double[] filter = transformToArray(impulseResponse);
        Complex[] filterSpectrum = computeSpectrum(filter);
        createWindows();
        processWindows(filterSpectrum);
        outputSignal = normalize(ArrayUtils.toPrimitive(outputSignal));
    }

    private Double[] normalize(double[] input) {
        Double[] normalized = new Double[input.length];
        double min = Arrays.stream(input).min().orElse(0.0);
        double max = Arrays.stream(input).max().orElse(0.0);
        double currentDifference = max - min;
        double desiredDifference = 0.8 - -0.8;
        double ratio = desiredDifference / currentDifference;
        for (int i = 0; i < input.length; i++) {
            normalized[i] = ratio * (input[i] - min) - 0.8;
        }

        return normalized;
    }

    private void createWindows() {
        for (int i = 0; i < samplesCount; i += R) {
            double[] window = createWindow(i);
            windows.add(window);
        }
    }

    private double[] createWindow(int from) {
        double[] window = new double[(int) M];
        for (int i = 0; i < M && from + i < samplesCount; i++) {
            window[i] = signal[from + i];
        }
        return window;
    }

    private Complex[] computeSpectrum(double[] array) {
        Complex[] complexArray = transformToComplexArray(array);
        FrequencyTransform transform = new FrequencyTransform(complexArray);
        transform.fft(false);
        return complexArray;
    }

    public Complex[] transformToComplexArray(double[] samples) {
        Complex[] res = new Complex[samples.length];
        for (int i = 0; i < samples.length; i++) {
            res[i] = new Complex(samples[i]);
        }
        return res;
    }

    private void processWindows(Complex[] filterSpectrum) {
        for (int i = 0; i < windows.size(); i++) {
            double[] window = windows.get(i);
            double[] processedWindow = processWindow(window, filterSpectrum);
            updateFilteredSamples((int) (i * R), processedWindow);
        }
    }

    private double[] processWindow(double[] window, Complex[] filterSpectrum) {
        double[] processedWindow = passThrowWindow(window);
        double[] expandedWindow = fillWithZeros(processedWindow);
        Complex[] windowSpectrum = computeSpectrum(expandedWindow);
        return performFiltering(windowSpectrum, filterSpectrum);
    }

    private double[] passThrowWindow(double[] input) {
        int N = input.length;
        double[] res = new double[N];
        for (int i = 0; i < N; i++) {
            double multiplier = windowType.getValue(i, N);
            res[i] = input[i] * multiplier;
        }

        return res;
    }

    private double[] performFiltering(Complex[] input, Complex[] filter) {
        Complex[] multiplicationResult = new Complex[input.length];
        for (int i = 0; i < input.length; i++) {
            multiplicationResult[i] = input[i].multiply(filter[i]);
        }
        FrequencyTransform transform = new FrequencyTransform(multiplicationResult);
        transform.ifft(false);
        return transform.realPart();
    }

    private void updateFilteredSamples(int from, double[] processedWindow) {
        for (int i = 0; i < processedWindow.length && i + from < samplesCount; i++) {
            outputSignal[i + from] += processedWindow[i];
        }
    }

    private double[] transformToArray(List<Double> doubles) {
        double[] target = new double[doubles.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = doubles.get(i);
        }
        return target;
    }

//    @Override
//    public void computeFilter() {
//        System.out.println("Samples count " + samplesCount);
//        outputSignal = new Double[samplesCount];
//        for (int i = 0; i < outputSignal.length; i++) {
//            outputSignal[i] = 0.0;
//        }
//        Complex[] signalTab;
//        Complex[] impulseResponseComplex = new Complex[impulseResponse.size()];
//        Complex[] resultOfMultiplication;
//        resultsOfFilterOperations.clear();
//        for (int i = 0; i < impulseResponse.size(); i++) {
//            impulseResponseComplex[i] = new Complex(impulseResponse.get(i), 0.0);
//        }
//        impulseResponseComplex = Fourier.computeFastFourier(impulseResponseComplex, bits);
//
//        for (int windowIndex = 0; windowIndex < signalWindows.size(); windowIndex++) {
//            signalTab = new Complex[signalWindows.get(windowIndex).getSamples().size()];
////            System.out.println(signalTab.length);
//            for (int sampleIndex = 0; sampleIndex < signalTab.length; sampleIndex++) {
//                signalTab[sampleIndex] = new Complex(signalWindows.get(windowIndex).getSamples().get(sampleIndex), 0.0);
//            }
//            signalTab = Fourier.computeFastFourier(signalTab, bits);
//
//            resultOfMultiplication = new Complex[signalTab.length];
//            for (int i = 0; i < resultOfMultiplication.length; i++) {
//                resultOfMultiplication[i] = signalTab[i].multiply(impulseResponseComplex[i]);
//            }
//
//            resultOfMultiplication = Fourier.computeInverseFastFourier(resultOfMultiplication, bits);
//            Double[] d = new Double[resultOfMultiplication.length];
//            for (int i = 0; i < d.length; i++) {
//                d[i] = resultOfMultiplication[i].getReal();
//            }
//            resultsOfFilterOperations.add(d);
//            if (windowIndex == signalWindows.size() - 1) {
//                //ChartDrawer.drawChart(signalTab, "signal spec" + windowIndex);
//                //ChartDrawer.drawChart(impulseResponseComplex, "impulse resp spec" + windowIndex);
//                //ChartDrawer.drawChart(d, "result of inverse from mul" + windowIndex);
//            }
//        }
//
//        System.out.println("Windows = " + resultsOfFilterOperations.size());
//        System.out.println("OutputSize = " + outputSignal.length);
//        System.out.println("Size of one window = " + resultsOfFilterOperations.get(0).length);
//        System.out.println("HOP Size = " + R);
//        for (int i = 0; i < resultsOfFilterOperations.size(); i++) {
//            System.out.println((i + 1) + " Elem = " + (i * R));
//            addElems((int) (i * R), resultsOfFilterOperations.get(i), outputSignal);
//        }
//    }

}
