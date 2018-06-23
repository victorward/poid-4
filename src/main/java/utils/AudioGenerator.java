package utils;

public class AudioGenerator {

    public Audio createTone(double frequency, double amplitude, double duration, long samplingRate) {
        double[] samples = new double[Math.toIntExact(Math.round(Math.ceil(duration * samplingRate)))];
        double samplesPerPeriod = samplingRate / frequency;
        double coefficient = 2 * Math.PI / samplesPerPeriod;
        for (int i = 0; i < samples.length; i++) {
            double angle = i * coefficient;
            samples[i] = Math.sin(angle) * amplitude;
        }
        Audio.Channel[] channels = {new Audio.Channel(samples)};
        return new Audio(1, samplingRate, channels);
    }

    public Audio createMultiTone(double[] frequencies, double amplitude, double duration, long samplingRate, int windowSize) {
        double[] samples = new double[Math.toIntExact(Math.round(Math.ceil(duration * samplingRate)))];
        for (int i = 0; i < frequencies.length; i++) {
//            double frequency = frequencies[i];
            double frequency = findClosestMatchingFrequency(frequencies[i], windowSize, samplingRate);
            double samplesPerPeriod = samplingRate / frequency;
            double coefficient = 2 * Math.PI / samplesPerPeriod;
            for (int j = 0; j < windowSize; j++) {
                int index = i * windowSize + j;
                if (index >= samples.length) {
                    break;
                }
                double angle = index * coefficient;
                samples[index] = Math.sin(angle) * amplitude;
            }
        }
        Audio.Channel[] channels = {new Audio.Channel(samples)};
        return new Audio(1, samplingRate, channels);
    }

    private double findClosestMatchingFrequency(double frequency, int windowSize, long samplingRate) {
        double matchingFrequency = Double.MAX_VALUE;
        for (int i = 10; i < windowSize; i++) {
            double currentFrequency = i * (1.0 * samplingRate / windowSize);
            if (Math.abs(currentFrequency - frequency) < Math.abs(matchingFrequency - frequency)) {
                matchingFrequency = currentFrequency;
            }
        }
        return matchingFrequency;
    }

    public Audio createAudio(double[] samples, long samplingRate) {
        Audio.Channel[] channels = {
                new Audio.Channel(samples)
        };
        return new Audio(1, samplingRate, channels);
    }
}
