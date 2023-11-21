package neurex.ann;

import java.io.Serial;
import java.io.Serializable;

public class Layer implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	public Connection[][] connections;
	public Neuron[] input;
	public Neuron[] output;

	public Layer(Neuron[] input, Neuron[] output) {
		// TODO Auto-generated constructor stub
        this.input = input;
        this.output = output;
        this.connections = new Connection[input.length][output.length];
        for (int i=0; i < input.length; i++) {
        	for (int j=0; j < output.length; j++) {
        		Connection connection = new Connection(this.input[i], this.output[j]);
        		connections[i][j] = connection;
        	}
        }
	}
	
	public void transfer() {
        for (Neuron neuron : output) {
            neuron.reset();
        }

        for (Connection[] connection : connections) {
            for (Connection value : connection) {
                value.transfer();
            }
        }

        for (Neuron neuron : output) {
            neuron.activate();
        }

	}
	
	public void errorPropagation() {
		for (int i=0; i < connections.length; i++) {
			double lowerDelta = 0.0;
			for (int j=0; j < connections[i].length; j++) {
				Neuron second = connections[i][j].second;
				double delta = second.delta;
				double slope = second.slope;
				double y = second.excitation();
				double w = connections[i][j].weight;
		        lowerDelta += delta*slope*y*(1.0-y)*w;
			}
			input[i].delta = lowerDelta;
		}
	}
	
	public void adapt(double eta) {
		this.errorPropagation();
        for (Connection[] connection : connections) {
            for (Connection con : connection) {
                double delta = con.second.delta;
                double y = con.second.excitation();
                double xi = con.first.excitation();
                double changeW = -1 * eta * delta * y * (1 - y) * xi;
                con.adapt(changeW);
            }
        }
        for (Neuron neuron : output) {
            double delta = neuron.delta;
            double slope = neuron.slope;
            double threshold = neuron.threshold;
            double potential = neuron.potential;
            double y = neuron.excitation();
            double changeSlope = -1 * eta * delta * y * (1 - y) * (potential - threshold);
            double changeThreshold = -1 * eta * delta * y * (1 - y) * (-1 * slope);
            neuron.adaptSlope(changeSlope);
            neuron.adaptThreshold(changeThreshold);
        }
	}
	
	
	public void dump() {
		for (int i=0; i < connections.length; i++) {
			for (int j=0; j < connections[i].length; j++) {
				System.out.println("w"+i+j+": "+connections[i][j].weight);
			}
		}
	}

}
