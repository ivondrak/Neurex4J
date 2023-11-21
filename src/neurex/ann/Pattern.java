package neurex.ann;

import java.io.Serial;
import java.io.Serializable;

public class Pattern implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	public double[] input;
	public double[] output;

	public Pattern(double[] input, double[] output) {
		this.input = input;
		this.output = output;
	}
	
	public double[] getInput() {
		return input;
	}
	
	public double[] getOutput() {
		return output;
	}

}
