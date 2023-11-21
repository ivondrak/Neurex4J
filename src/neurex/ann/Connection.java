package neurex.ann;

public class Connection {
	
	Neuron first;
	Neuron second;
	double weight = Math.random()/3.0;

	public Connection(Neuron first, Neuron second) {
		this.first = first;
		this.second = second;
	}
	
	public void adapt(double change) {
		this.weight += change;
	}
	
	public void transfer() {
		this.second.adjust(weight*first.excitation());
	}

}
