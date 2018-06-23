package utils;

public class Audio {
    public int numberOfChannels;
    public long sampleRate;
    public Channel[] channels;

    public Audio(int numberOfChannels, long sampleRate, Channel[] channels) {
        this.numberOfChannels = numberOfChannels;
        this.sampleRate = sampleRate;
        this.channels = channels;

    }

    public static class Channel {
        public double[] samples;
        public double amplitude;

        public Channel(double[] samples) {
            this.samples = samples;
            amplitude = samples[0];
            for (int i = 0; i < samples.length; i++) {
                if (samples[i] > amplitude) {
                    amplitude = samples[i];
                }
            }
        }
    }
}


