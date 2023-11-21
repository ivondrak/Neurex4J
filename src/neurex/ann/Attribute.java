package neurex.ann;

import java.io.Serial;
import java.io.Serializable;

public class Attribute implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
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
