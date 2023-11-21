package neurex.ann;

public class Pattern {
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
