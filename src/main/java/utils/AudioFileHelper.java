package utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.WavFile.WavFile;
import utils.WavFile.WavFileException;

import java.io.File;
import java.io.IOException;

public class AudioFileHelper {

    public Audio readFile(Stage stage) {
        File file = loadFIle(stage);
        return readFile(file);
    }

    public Audio readFile(File file) {
        try {
            WavFile wavFile = WavFile.openWavFile(file);
//            wavFile.display(); // shows info about wav file
            int numberOfChannels = wavFile.getNumChannels();
            long sampleRate = wavFile.getSampleRate();
            int numberOfFrames = Math.toIntExact(wavFile.getNumFrames());
            double[][] frames = new double[numberOfChannels][numberOfFrames];
            wavFile.readFrames(frames, numberOfFrames);
            wavFile.close();
            Audio.Channel[] channels = new Audio.Channel[numberOfChannels];
            for (int i = 0; i < frames.length; ++i) {
                channels[i] = new Audio.Channel(frames[i]);
            }
            return new Audio(
                    numberOfChannels,
                    sampleRate,
                    channels
            );
        } catch (IOException | WavFileException e) {
            e.printStackTrace();
            return new Audio(
                    0,
                    0,
                    new Audio.Channel[0]
            );
        }
    }

    public void saveFile(Audio audio, Stage stage) {
        File file = selectSaveFile(stage);
        saveFile(audio, file);
    }

    public void saveFile(Audio audio, File file) {
        try {
            WavFile wavFile = WavFile.newWavFile(
                    file,
                    audio.numberOfChannels,
                    audio.channels[0].samples.length,
                    16,
                    audio.sampleRate
            );
            int offset = 0;
            for (Audio.Channel channel : audio.channels) {
                wavFile.writeFrames(channel.samples, offset, channel.samples.length);
                offset += channel.samples.length;
            }
            wavFile.close();
        } catch (IOException | WavFileException e) {
            e.printStackTrace();
        }
    }

    private File loadFIle(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("P:\\sounds"));
        fileChooser.setTitle("Open Resource File");
        return fileChooser.showOpenDialog(stage);
    }

    private File selectSaveFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save resource file");
        return fileChooser.showSaveDialog(stage);
    }
}
