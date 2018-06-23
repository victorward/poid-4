package operations.soi;

import java.util.ArrayList;
import java.util.List;

public class SoundSignal {
    private List<Double> samples;

    public SoundSignal(int startingIndex, int windowSize, double[] samples) {
        this.samples = new ArrayList<>();

        for (int i = startingIndex; i < startingIndex + windowSize; i++) {
            if (i < samples.length) {
                this.samples.add(samples[i]);
            } else {
                this.samples.add(0.0);
            }
        }
    }

    public List<Double> getSamples() {
        return samples;
    }

    public void setSamples(List<Double> samples) {
        this.samples = samples;
    }


}