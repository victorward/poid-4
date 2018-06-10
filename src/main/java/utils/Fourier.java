package utils;

import org.apache.commons.math3.complex.Complex;

public class Fourier {
    public static Complex[] computeFastFourier(Complex[] sound, int bits) {
        double N = sound.length;

        Complex[] transformedSignal = new Complex[(int) N];

        for (int i = 0; i < transformedSignal.length; i++) {
            transformedSignal[i] = new Complex(0.0, 0.0);
        }

        Complex signalTab[] = new Complex[(int) N];
        Complex[] localTR = new Complex[(int) N];
        int index = 0;
        for (int i = 0; i < sound.length; i++) {
            signalTab[index] = new Complex(sound[i].getReal(), sound[i].getImaginary());
            index++;
        }

        index = 0;
        for (Complex cv : signalTab) {
//            System.out.println("x(" + index + ") = " + cv.getReal() + " IM: i" + cv.getImaginary());
            index++;
        }
        //Zmienna określająca na jakiej wielkości ma operować na tablicy
        int part = 2;
        //Pętla określająca cykl przechodzenia, przez kolejne kolumny
        for (int iteration = 1; iteration <= bits; iteration++) {
//            System.out.println("PART "+part);
            //Ile razy ma się wykonać
            for (int i = 0; i < part; i += 2) {

                int r = 0;
                for (int actualIndex = (signalTab.length / part) * i, counter = 0; counter < signalTab.length / part; counter++, actualIndex++) {
                    int secondIndex = (actualIndex + (signalTab.length / part));
                    Complex a = signalTab[actualIndex].add(signalTab[secondIndex]);
                    Complex b = signalTab[actualIndex].subtract(signalTab[secondIndex]);
                    Complex W = new Complex(Math.cos((2.0 * Math.PI * r) / N), -Math.sin((2.0 * Math.PI * r) / N));
                    b = b.multiply(W);
                    signalTab[actualIndex] = a;
                    signalTab[secondIndex] = b;
                    r += part - (part / 2);
                }
            }
            part += part;
        }

        localTR[0] = signalTab[0];
        localTR[localTR.length - 1] = signalTab[signalTab.length - 1];
        for (int i = 1; i < signalTab.length - 1; i++) {
            String bitIndex = Integer.toBinaryString(i);
            if (bitIndex.length() < bits) {
                while (bitIndex.length() < bits) {
                    bitIndex = "0" + bitIndex;
                }
            }
            char[] tab = bitIndex.toCharArray();

            for (int j = 0; j < tab.length / 2; j++) {
                char temp = tab[j];
                tab[j] = tab[tab.length - j - 1];
                tab[tab.length - j - 1] = temp;
            }
            bitIndex = new String(tab);
            localTR[Integer.parseInt(bitIndex, 2)] = signalTab[i];
        }
        for (int i = 0; i < localTR.length; i++) {
            transformedSignal[i] = new Complex(localTR[i].getReal(), localTR[i].getImaginary());
        }

        return transformedSignal;
    }

    public static Complex[] computeInverseFastFourier(Complex[] sound, int bits) {
        double N = sound.length;

        Complex[] transformedSignal = new Complex[(int) N];


        for (int i = 0; i < transformedSignal.length; i++) {
            transformedSignal[i] = new Complex(0.0, 0.0);
        }


        Complex signalTab[] = new Complex[(int) N];
        Complex[] localTR = new Complex[(int) N];
        int index = 0;
        for (int i = 0; i < sound.length; i++) {
            signalTab[index] = new Complex(sound[i].getReal(), sound[i].getImaginary());
            index++;
        }

        index = 0;
        for (Complex cv : signalTab) {
//            System.out.println("x(" + index + ") = " + cv.getReal() + " IM: i" + cv.getImaginary());
            index++;
        }
        //Zmienna określająca na jakiej wielkości ma operować na tablicy
        int part = 2;
        //Pętla określająca cykl przechodzenia, przez kolejne kolumny
        for (int iteration = 1; iteration <= bits; iteration++) {
//            System.out.println("PART "+part);
            //Ile razy ma się wykonać
            for (int i = 0; i < part; i += 2) {

                int r = 0;
                for (int actualIndex = (signalTab.length / part) * i, counter = 0; counter < signalTab.length / part; counter++, actualIndex++) {
                    int secondIndex = (actualIndex + (signalTab.length / part));
                    Complex a = signalTab[actualIndex].add(signalTab[secondIndex]);
                    Complex b = signalTab[actualIndex].subtract(signalTab[secondIndex]);
                    Complex W = new Complex(Math.cos((2.0 * Math.PI * r) / N), Math.sin((2.0 * Math.PI * r) / N));
                    b = b.multiply(W);
                    signalTab[actualIndex] = a;
                    signalTab[secondIndex] = b;
                    r += part - (part / 2);
                }
            }
            part += part;
        }

        localTR[0] = signalTab[0];
        localTR[localTR.length - 1] = signalTab[signalTab.length - 1];
        for (int i = 1; i < signalTab.length - 1; i++) {
            String bitIndex = Integer.toBinaryString(i);
            if (bitIndex.length() < bits) {
                while (bitIndex.length() < bits) {
                    bitIndex = "0" + bitIndex;
                }
            }
            char[] tab = bitIndex.toCharArray();

            for (int j = 0; j < tab.length / 2; j++) {
                char temp = tab[j];
                tab[j] = tab[tab.length - j - 1];
                tab[tab.length - j - 1] = temp;
            }
            bitIndex = new String(tab);
            localTR[Integer.parseInt(bitIndex, 2)] = signalTab[i];
        }
        for (int i = 0; i < localTR.length; i++) {
            transformedSignal[i] = new Complex(localTR[i].getReal() / N, localTR[i].getImaginary() / N);
        }
        return transformedSignal;
    }
}