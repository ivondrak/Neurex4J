package neurex.ann;

public class Attribute {
	public String attribute;
	public double minValue;
	public double maxValue;
	
	public Attribute() {
		attribute = "";
		minValue = 0;
		maxValue = 1;
	}

	public Attribute(String attribute, double minValue, double maxValue) {
		// TODO Auto-generated constructor stub
		this.attribute = attribute;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

}
