package operations;

public enum WindowType {
    SQUARE("Square"), VON_HANN("Hanning"), HAMMING("Hamming");

    private String windowType;

    private WindowType(String windowType) {
        this.windowType = windowType;
    }

    @Override
    public String toString() {
        return windowType;
    }

    public double getValue(double n, double windowSize) {
        double result = 0;

        if (windowType.equals(WindowType.SQUARE.toString())) {
            return 1.0;
        }

        if (windowType.equals(WindowType.VON_HANN.toString())) {
            return 0.5 * (1 - Math.cos((2 * Math.PI * n) / (windowSize - 1)));
//            return 0.5 - (0.5 * Math.cos((2 * Math.PI * n) / (windowSize - 1.0)));
        }

        if (windowType.equals(WindowType.HAMMING.toString())) {
            return 0.54 - (0.46 * Math.cos((2 * Math.PI * n) / (windowSize - 1.0)));
        }

        return result;
    }
}
