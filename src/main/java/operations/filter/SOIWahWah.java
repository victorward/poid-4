package operations.filter;

import operations.WindowType;
import org.apache.commons.math3.complex.Complex;
import utils.Fourier;

public class SOIWahWah extends SOIFilter {

    private double frequency;
    private double width;
    private double amplify;
    private double leftSideFreq;
    private double rightSideFreq;
    private double[] oscilationFunction;
    private double samplesInOnePeriod = SAMPLING_FREQUENCY / frequency;
    private double timeBetweenOneSamples = 1.0 / SAMPLING_FREQUENCY;

    @Override
    public void computeFilter() {
//        outputSignal = new Double[(int) N];
//        outputSignal = new Double[samplesCount];
//        for (int i = 0; i < outputSignal.length; i++) {
//            outputSignal[i] = 0.0;
//        }
//        Complex[] signalTab;
//        Complex[] impulseResponseComplex = new Complex[impulseResponse.size()];
//        Complex[] resultOfMultiplication;
//        resultsOfFilterOperations.clear();
//
//        for (int windowIndex = 0; windowIndex < signalWindows.size(); windowIndex++) {
//            signalTab = new Complex[signalWindows.get(windowIndex).getSamples().size()];
//            for (int sampleIndex = 0; sampleIndex < signalTab.length; sampleIndex++) {
//                signalTab[sampleIndex] = new Complex(signalWindows.get(windowIndex).getSamples().get(sampleIndex), 0.0);
//            }
//            signalTab = Fourier.computeFastFourier(signalTab, bits);
//            int t = (int) (windowIndex * M);
//            double fc = oscilationFunction[t];
//
//            double leftSide = (fc - width / 2.0) / (SAMPLING_FREQUENCY / N);
//            double rightSide = (fc + width / 2.0) / (SAMPLING_FREQUENCY / N);
//            for (int i = 0; i < signalTab.length / 2; i++) {
//                if (i > leftSide && i < rightSide) {
//                    signalTab[i] = signalTab[i].multiply(amplify);
//                    signalTab[signalTab.length - i - 1] = signalTab[i].multiply(amplify);
//                } else {
//                    signalTab[i] = signalTab[i].multiply(1.0);
//                    signalTab[signalTab.length - i - 1] = signalTab[i].multiply(1.0);
//                }
//            }
//
//            resultOfMultiplication = new Complex[signalTab.length];
//            for (int i = 0; i < resultOfMultiplication.length; i++) {
//                resultOfMultiplication[i] = signalTab[i];
//            }
//
//            resultOfMultiplication = Fourier.computeInverseFastFourier(resultOfMultiplication, bits);
//            Double[] d = new Double[resultOfMultiplication.length];
//            for (int i = 0; i < d.length; i++) {
//                d[i] = resultOfMultiplication[i].getReal();
//            }
//            resultsOfFilterOperations.add(d);
//        }
//        for (int i = 0; i < resultsOfFilterOperations.size(); i++) {
//            System.out.println((i + 1) + " Elem = " + (i * R));
//            addElems((int) (i * R), resultsOfFilterOperations.get(i), outputSignal);
//        }
    }

    @Override
    public void setSignalWindows(Integer[] samples) {
        super.setSignalWindows(samples); //To change body of generated methods, choose Tools | Templates.
        oscilationFunction = new double[samples.length];
        oscilationFunction[0] = leftSideFreq;
        for (int i = 1; i < oscilationFunction.length; i++) {
            oscilationFunction[i] = oscilationFunction[i - 1]
                    + ((rightSideFreq - leftSideFreq))
                    * Math.signum(Math.sin(2.0 * Math.PI * i * frequency / SAMPLING_FREQUENCY));
        }
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getAmplify() {
        return amplify;
    }

    public void setAmplify(double amplify) {
        this.amplify = amplify;
    }

    public double getLeftSideFreq() {
        return leftSideFreq;
    }

    public void setLeftSideFreq(double leftSideFreq) {
        this.leftSideFreq = leftSideFreq;
    }

    public double getRightSideFreq() {
        return rightSideFreq;
    }

    public void setRightSideFreq(double rightSideFreq) {
        this.rightSideFreq = rightSideFreq;
    }

    @Override
    public void setWindowType(WindowType windowType) {

    }

    @Override
    public void setFc(double fc) {

    }
}
