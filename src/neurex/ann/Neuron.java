package neurex.ann;

import java.io.Serial;
import java.io.Serializable;

public class Neuron implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	double state;
	double potential = 0.0;
	double delta = 0.0;
	double slope = 1.0;
	double threshold = 0.0;
	
	public Neuron(double state) {
		this.state = state;
	}
	
	public void setState(double state) {
		this.state = state;
	}
	
	public double getState() {
		return state;
	}
	
	public double excitation() {
		return state;
	}
	
	public void reset() {
		this.potential = 0.0;
	}
	
	public void adjust(double signal) {
		potential += signal;
	}
	
	public void adaptSlope(double change) {
		slope += change;
	}
	
	public void adaptThreshold(double change) {
		threshold += change;
	}
	
	public void activate() {
		state = 1/(1+Math.exp(-slope*(potential-threshold)));
	}

}
