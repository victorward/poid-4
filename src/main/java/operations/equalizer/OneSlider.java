package operations.equalizer;

public class OneSlider {
    private double leftSideOfFrequencies;
    private double rightSideOfFrequencies;

    private double edge = 1;

    OneSlider(double leftSideOfFrequencies, double rightSideOfFrequencies) {
        this.leftSideOfFrequencies = leftSideOfFrequencies;
        this.rightSideOfFrequencies = rightSideOfFrequencies;
    }

    public double getEdge() {
        return edge;
    }

    public void setEdge(double edge) {
        this.edge = edge;
    }

    public double getLeftSideOfFrequencies() {
        return leftSideOfFrequencies;
    }

    public double getRightSideOfFrequencies() {
        return rightSideOfFrequencies;
    }
}

