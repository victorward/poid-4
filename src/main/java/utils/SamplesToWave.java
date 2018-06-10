package utils;

import com.sun.media.sound.WaveFileWriter;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SamplesToWave {
    private double samplingFrequency;
    private List<Integer> samples;
    private AudioFormat audioFormat;

    public SamplesToWave(double samplingFrequency, List<Double> samples, AudioFormat audioFormat) {
        this.samplingFrequency = samplingFrequency;
        this.samples = new ArrayList<>();
        for (int i = 0; i < samples.size(); i++) {
            if (samples.get(i) != 0) {
                this.samples.add(samples.get(i).intValue());
            }
        }

        this.audioFormat = audioFormat;
    }

    public SamplesToWave(double samplingFrequency, Double[] samples, AudioFormat audioFormat) {
        this.samplingFrequency = samplingFrequency;
        this.samples = new ArrayList<>();
        for (int i = 0; i < samples.length; i++) {
            this.samples.add(samples[i].intValue());
        }

        this.audioFormat = audioFormat;
    }

    public void saveWave(String path) throws FileNotFoundException, IOException {

        System.out.println("Samples ALL " + samples.size());
        byte[] bytesToSave = new byte[samples.size() * 2];

        int count = 0;
//        for (int i = 0; i < samples.size(); i++) {
//
////            BigDecimal bi = new BigDecimal(samples.get(i));
//            BigInteger bi = new BigInteger(samples.get(i).toString());
//            byte[] b = bi.toByteArray();
////            System.out.println("Byte size "+b.length);
//            if (b.length == 1) {
//                bytesToSave[count+1] = 0;
//                bytesToSave[count] = b[0];
//            } else {
//                bytesToSave[count+1] = b[0];
//                bytesToSave[count] = b[1];
//            }
//            count += 2;
//        }
        for (int i = 0; i < samples.size(); i++) {
            int temp = (short) samples.get(i).intValue();
            bytesToSave[2 * i] = (byte) temp;
            bytesToSave[2 * i + 1] = (byte) (temp >> 8);
        }
        WaveFileWriter wfw = new WaveFileWriter();
        InputStream b_in = new ByteArrayInputStream(bytesToSave);
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(path));
        dos.write(bytesToSave);
        File file = new File(path);
        AudioInputStream stream = new AudioInputStream(b_in, audioFormat,
                bytesToSave.length / 2);
//        AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
        wfw.write(stream, AudioFileFormat.Type.WAVE, file);

//        b_in.close();
//        dos.close();
//        stream.close();
    }
}