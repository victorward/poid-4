package utils;

import org.apache.commons.math3.complex.Complex;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static utils.FourierTransformType.INVERSE;
import static utils.FourierTransformType.NORMAL;

enum FourierTransformType {
    NORMAL, INVERSE
}

public class FrequencyTransform {

    private Complex[] input;

    public FrequencyTransform(Complex[] input) {
        this.input = input;
    }

    private void coreFourierTransform(Complex[] input, FourierTransformType type) {
        int n = input.length;
        if (n == 1) {
            return;
        }
        int coefficient = type == NORMAL ? 1 : -1;
        Complex w = new Complex(1.0);
        Complex wN = Complex.I.multiply(2).multiply(Math.PI).multiply(coefficient).divide(n).exp();
        Complex[] x1 = new Complex[n / 2]; //even
        Complex[] x2 = new Complex[n / 2]; //odd
        for (int index = 0; index < n; ++index) {
            if ((index & 1) == 1) {
                x2[index / 2] = input[index];
            } else {
                x1[index / 2] = input[index];
            }
        }

        coreFourierTransform(x1, type);
        coreFourierTransform(x2, type);
        for (int index = 0; index < n / 2; ++index) {
            input[index] = w.multiply(x2[index]).add(x1[index]);
            input[index + n / 2] = x1[index].subtract(w.multiply(x2[index]));
            w = w.multiply(wN);
        }
    }

    public void fft() {
        coreFourierTransform(input, NORMAL);
        cutInHalf();
    }

    public void fft(boolean shouldCutInHalf) {
        coreFourierTransform(input, NORMAL);
        if (shouldCutInHalf) {
            cutInHalf();
        }
    }

    private void cutInHalf() {
        Complex[] half = new Complex[input.length / 2];
        for (int i = 0; i < input.length / 2; i++) {
            half[i] = input[i];
        }
        input = half;
    }

    public void ifft() {
        coreFourierTransform(input, INVERSE);
        int n = input.length;
        for (int i = 0; i < n; ++i) {
            input[i] = input[i].divide(n);
        }
        cutInHalf();
    }

    public void ifft(boolean shouldCutInHalf) {
        coreFourierTransform(input, INVERSE);
        int n = input.length;
        for (int i = 0; i < n; ++i) {
            input[i] = input[i].divide(n);
        }
        if (shouldCutInHalf) {
            cutInHalf();
        }
    }

    public double[] amplitudeSpectrum() {
        int rows = input.length;
        double[] res = new double[rows];
        for (int i = 0; i < rows; ++i) {
            Complex pixel = input[i];
            double amplitude = pixel.abs();
//            double amplitude = temp(pixel);
            res[i] = amplitude;
        }
//        return normalize(res, true);
        return res;
    }

    public int[] phaseSpectrum() {
        int rows = input.length;
        double[] res = new double[rows];
        for (int i = 0; i < rows; ++i) {
            Complex pixel = input[i];
            double phase = pixel.getArgument();
            res[i] = phase;
        }
        return normalize(res, false);
    }

    private int[] normalize(double[] input, boolean shouldLog) {
        int rows = input.length;

        double min = input[0];
        double max = min;
        for (int i = 1; i < rows; ++i) {
            if (input[i] != 0 && (min == 0 || input[i] < min)) {
                min = input[i];
            }
            if (input[i] > max) {
                max = input[i];
            }
        }
        max = shouldLog ? Math.log(max) : max;
        min = shouldLog ? Math.log(min) : min;

        int[] res = new int[rows];
        for (int i = 0; i < rows; ++i) {
            double normalizedDoubleValue = (1.0 / (max - min))
                    * ((shouldLog ? (input[i] == 0 ? Math.log(min) : Math.log(input[i])) : input[i]) - min);
            int normalizedPixel = Math.toIntExact(Math.round(Math.ceil(normalizedDoubleValue * 255)));
            res[i] = max(min(normalizedPixel, 255), 0);
        }
        return res;
    }

    public double[] realPart() {
        double[] real = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            real[i] = input[i].getReal();
        }
        return real;
    }
}
