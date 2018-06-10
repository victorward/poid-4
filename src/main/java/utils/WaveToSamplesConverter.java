package utils;

import com.sun.media.sound.WaveFileReader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class WaveToSamplesConverter {

    public static Integer[] convertWaveToIntSamples(File waveFile) throws IOException, UnsupportedAudioFileException {
        WaveFileReader wfr = new WaveFileReader();
        AudioInputStream ais = wfr.getAudioInputStream(waveFile);
        AudioFormat af = ais.getFormat();

        int sampleSize = af.getSampleSizeInBits();
        List<Integer> samples = new ArrayList<>();
        byte[] b = new byte[sampleSize/8];
        while(ais.available() > 0){
            ais.read(b);
            byte aa = b[0];
            byte bb = b[1];
            b[0] = bb;
            b[1] = aa;
            samples.add(new BigInteger(b).intValue());
        }
        Integer[] a = samples.toArray(new Integer[samples.size()]);
        ais.close();
        return a;
    }

    public static AudioFormat getAudioFormat(File waveFile) throws IOException, UnsupportedAudioFileException {
        WaveFileReader wfr = new WaveFileReader();
        AudioInputStream ais = wfr.getAudioInputStream(waveFile);
        AudioFormat af = ais.getFormat();
        return af;
    }

    public static double getSamplingFrequency(File waveFile) throws IOException, UnsupportedAudioFileException {
        WaveFileReader wfr = new WaveFileReader();
        AudioInputStream ais = wfr.getAudioInputStream(waveFile);
        AudioFormat af = ais.getFormat();
        return af.getFrameRate();
    }

}