package neurex.ann;

public class NeuralNet {
	
	public TrainingSet trainingSet;
	public Attribute[][] attributes;
	public Layer[] layers;
	public Neuron[][] neurons;
	public int inputSize, outputSize, innerSize, hidden;
	

	public NeuralNet() {
		// TODO Auto-generated constructor stub
		inputSize = 1;
		outputSize = 1;
		innerSize = 1;
		hidden = 2;
		trainingSet = new TrainingSet();
		attributes = new Attribute[2][1];
		for (int i=0; i < 2; i++) {
			attributes[i][0] = new Attribute();
		}
		layers = new Layer[hidden+1];
		neurons = new Neuron[hidden+2][];
		int last = hidden+1;
		for (int i=0; i < (hidden+2); i++) {
			if (i == 0) {
				neurons[i] = new Neuron[inputSize];
				for (int index=0; index < inputSize; index++) {
					neurons[i][index] = new Neuron(0);
				}
			} else if (i == last) {
				neurons[i] = new Neuron[outputSize];
				for (int index=0; index < outputSize; index++) {
					neurons[i][index] = new Neuron(0);
				}
			} else {
				neurons[i] = new Neuron[innerSize];
				for (int index=0; index < innerSize; index++) {
					neurons[i][index] = new Neuron(0);
				}
			}
		}
		for (int i=0; i < (hidden+1); i++) {
			layers[i] = new Layer(neurons[i], neurons[i+1]);
		}
	}
	
	public NeuralNet(Attribute[][] attributes, TrainingSet training, int hidden) {
		// TODO Auto-generated constructor stub
		this.attributes = attributes;
		this.trainingSet = training;
		this.inputSize  = attributes[0].length;
		this.outputSize = attributes[1].length;
		this.innerSize = (inputSize > outputSize) ? inputSize : outputSize;
		this.hidden = hidden;
		layers = new Layer[hidden+1];
		neurons = new Neuron[hidden+2][];
		int last = hidden+1;
		for (int i=0; i < (hidden+2); i++) {
			if (i == 0) {
				neurons[i] = new Neuron[inputSize];
				for (int index=0; index < inputSize; index++) {
					neurons[i][index] = new Neuron(0);
				}
			} else if (i == last) {
				neurons[i] = new Neuron[outputSize];
				for (int index=0; index < outputSize; index++) {
					neurons[i][index] = new Neuron(0);
				}
			} else {
				neurons[i] = new Neuron[innerSize];
				for (int index=0; index < innerSize; index++) {
					neurons[i][index] = new Neuron(0);
				}
			}
		}
		for (int i=0; i < (hidden+1); i++) {
			layers[i] = new Layer(neurons[i], neurons[i+1]);
		}
	}
	
	public double[] input() {
		double[] vector = new double[inputSize];
		for (int i=0; i < inputSize; i++) {
			vector[i] = neurons[0][i].excitation();
		}
		return vector;
	}
	
	public double[] output() {
		double[] vector = new double[outputSize];
		for (int i=0; i < outputSize; i++) {
			vector[i] = neurons[hidden+1][i].excitation();
		}
		return vector;
	}
	
	public double[] run(double[] input) {
		double output[] = new double[inputSize];
		for (int i=0; i < inputSize; i++) {
			neurons[0][i].setState(input[i]);
		}
		for (int i=0; i < (hidden+1); i++) {
			layers[i].transfer();
		}
		for (int i=0; i < outputSize; i++) {
			output[i] = neurons[hidden+1][i].excitation();
		}
		return output();
	}
	
	public void learn(double eta, int cycles) {
		for (int i=0; i < cycles; i++) {
			for (int index=0; index < trainingSet.patterns.length; index++) {
				Pattern pattern = trainingSet.patterns[index];
				this.run(pattern.getInput());
				for (int j=0; j < outputSize; j++) {
					Neuron neuron = neurons[hidden+1][j];
					neuron.delta = neuron.excitation() - pattern.getOutput()[j];
				}
				for (int j=hidden; j >= 0; j--) {
					layers[j].adapt(eta);
				}
			}
		}
	}
	
	public Number[] meanSquaredError() {
		Number[] errorVector = new Number[3];
		int n = trainingSet.patterns.length;
		double error = 0.0;
		int worst = -1;
		double maxError = 0.0;
		for (int i=0; i < n; i++) {
			Pattern pattern = trainingSet.patterns[i];
			double[] actual = this.run(pattern.input);
			for (int index=0; index < actual.length; index++) {
				double actualError = Math.pow(actual[index] - pattern.output[index], 2);
				if (actualError > maxError) {
					maxError = actualError;
					worst = i;
				}
				error = error + actualError;
			}
		}
		error = (Math.sqrt(error)/n)*100/outputSize;
		maxError = (Math.sqrt(maxError))*100/outputSize;
		errorVector[0] = error;
		errorVector[1] = maxError;
		errorVector[2] = worst;
		return errorVector;
	}
		
	
	public void dump() {
		for (int i=0; i < inputSize; i++) {
			String attribute = attributes[0][i].attribute;
			double minValue = attributes[0][i].minValue;
			double maxValue = attributes[0][i].maxValue;
			System.out.println("Input attributes: ("+attribute+", "+minValue+", "+maxValue+")");
		}
		for (int i=0; i < outputSize; i++) {
			String attribute = attributes[1][i].attribute;
			double minValue = attributes[1][i].minValue;
			double maxValue = attributes[1][i].maxValue;
			System.out.println("Output attributes: ("+attribute+", "+minValue+", "+maxValue+")");
		}
		for (int i=0; i < neurons.length; i++) {
			System.out.println("Layer of neurons #"+i);
			for (int j=0; j < neurons[i].length; j++) {
				Neuron neuron = neurons[i][j];
				System.out.println("Neuron"+j+": state: "+neuron.excitation()+" slope: "+neuron.slope+" threshold: "+neuron.threshold);
			}
		}
		for (int i=0; i < layers.length; i++) {
			System.out.println("Layer of connections #"+i);
			layers[i].dump();
		}
	}
}
