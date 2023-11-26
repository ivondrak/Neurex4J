package neurex.ann;

public class AttributePair {
    public Attribute attribute;
    public double value;

    public AttributePair(Attribute attribute, double value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public String toString() {
        double realValue = attribute.minValue + (attribute.maxValue - attribute.minValue) * value;
        double rounded = Math.round(realValue * 100.0) / 100.0;
        return attribute.attribute + ": " + rounded;
    }
}

