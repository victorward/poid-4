package operations.filter;

import org.apache.commons.math3.complex.Complex;
import utils.Fourier;

public class SOIFilterSpectrum extends SOIFilter {

    @Override
    public void computeFilter() {
        System.out.println("Samples count " + samplesCount);
        outputSignal = new Double[samplesCount];
        for (int i = 0; i < outputSignal.length; i++) {
            outputSignal[i] = 0.0;
        }
        Complex[] signalTab;
        Complex[] impulseResponseComplex = new Complex[impulseResponse.size()];
        Complex[] resultOfMultiplication;
        resultsOfFilterOperations.clear();
        for (int i = 0; i < impulseResponse.size(); i++) {
            impulseResponseComplex[i] = new Complex(impulseResponse.get(i), 0.0);
        }
        impulseResponseComplex = Fourier.computeFastFourier(impulseResponseComplex, bits);

        for (int windowIndex = 0; windowIndex < signalWindows.size(); windowIndex++) {
            signalTab = new Complex[signalWindows.get(windowIndex).getSamples().size()];
//            System.out.println(signalTab.length);
            for (int sampleIndex = 0; sampleIndex < signalTab.length; sampleIndex++) {
                signalTab[sampleIndex] = new Complex(signalWindows.get(windowIndex).getSamples().get(sampleIndex), 0.0);
            }
            signalTab = Fourier.computeFastFourier(signalTab, bits);

            resultOfMultiplication = new Complex[signalTab.length];
            for (int i = 0; i < resultOfMultiplication.length; i++) {
                resultOfMultiplication[i] = signalTab[i].multiply(impulseResponseComplex[i]);
            }

            resultOfMultiplication = Fourier.computeInverseFastFourier(resultOfMultiplication, bits);
            Double[] d = new Double[resultOfMultiplication.length];
            for (int i = 0; i < d.length; i++) {
                d[i] = resultOfMultiplication[i].getReal();
            }
            resultsOfFilterOperations.add(d);
            if (windowIndex == signalWindows.size() - 1) {
                //ChartDrawer.drawChart(signalTab, "signal spec" + windowIndex);
                //ChartDrawer.drawChart(impulseResponseComplex, "impulse resp spec" + windowIndex);
                //ChartDrawer.drawChart(d, "result of inverse from mul" + windowIndex);
            }
        }

        System.out.println("Windows = " + resultsOfFilterOperations.size());
        System.out.println("OutputSize = " + outputSignal.length);
        System.out.println("Size of one window = " + resultsOfFilterOperations.get(0).length);
        System.out.println("HOP Size = " + R);
        for (int i = 0; i < resultsOfFilterOperations.size(); i++) {
            System.out.println((i + 1) + " Elem = " + (i * R));
            addElems((int) (i * R), resultsOfFilterOperations.get(i), outputSignal);
        }
    }

}
