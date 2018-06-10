package operations.equalizer;

import java.util.ArrayList;
import java.util.List;

public class Equalizer {

    private List<OneSlider> sliders;

    public Equalizer() {
        sliders = new ArrayList<>();
        double side = 20;
        do {
            sliders.add(new OneSlider(side, side * 4));
            side *= 4;
        } while (side < 20000);
    }

    public List<OneSlider> getSliders() {
        return sliders;
    }

}
