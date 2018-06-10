package operations.soi;

import operations.WindowType;
import operations.equalizer.Equalizer;
import operations.filter.SOIFilter;
import operations.filter.SOIFilterConvolution;
import utils.SamplesToWave;
import utils.WaveToSamplesConverter;
import utils.chart.ChartDrawer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class MainTestes {

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {

        File f = new File("chirp_100Hz_1000Hz_lin.wav");
        Integer[] samples = WaveToSamplesConverter.convertWaveToIntSamples(f);
        AudioFormat af = WaveToSamplesConverter.getAudioFormat(f);
        double M = 8192;
        double R = M / 8;
        double L = 8192;
        ChartDrawer.drawChart(samples, "start signal");

        Equalizer eq = new Equalizer();
        eq.getSliders().get(0).setEdge(1);
        eq.getSliders().get(1).setEdge(1);
        eq.getSliders().get(2).setEdge(1);
//        SOIFilter filter = new SOIFilterEqualizer(eq);
//        SOIFilter filter = new SOIFilterSpectrum();
        SOIFilter filter = new SOIFilterConvolution();
        filter.setN(14);
        eq.getSliders().get(0).setEdge(5);
        eq.getSliders().get(1).setEdge(5);
        eq.getSliders().get(2).setEdge(-5); //skala
        //SOIFilter filter = new SOIFilterEqualizer(eq);

        //SOIFilter filter = new SOIFilterConvolution();
        filter.setN(13); //d
        filter.setM(M);
        filter.setR(R);
        filter.setL(L);
        filter.setFc(500); //d
        filter.setWindowType(WindowType.VON_HANN);
        System.out.println("Okna");
        filter.setSignalWindows(samples);
        System.out.println("Odpowiedź impulsowa");
        filter.computeLowPassFilterParameters();
        System.out.println("Widma");
        filter.computeFilter();
        ChartDrawer.drawChart(filter.getOutputSignal(), "output signal");
        SamplesToWave saver = new SamplesToWave(SOIFilter.SAMPLING_FREQUENCY, filter.getOutputSignal(), af);
        saver.saveWave("Sygnal.wav");
        System.out.println("Zapisane");
    }

}
