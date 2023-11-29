package neurex.ann;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class TrainingSet implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	public Pattern[] patterns;
	
	public TrainingSet() {
		double[] input = {0};
		double[] output = {0};
		Pattern pattern = new Pattern(input, output);
		patterns = new Pattern[1];
		patterns[0] = pattern;
	}

	public TrainingSet(Pattern[] patterns) {
		this.patterns = patterns;
	}
	
	public void addPattern() {
		double[] input = new double[patterns[0].input.length];
		double[] output = new double[patterns[0].output.length];
		Pattern pattern = new Pattern(input, output);
		Pattern[] newPatterns = Arrays.copyOf(patterns,patterns.length+1);
		newPatterns[patterns.length] = pattern;
		patterns = newPatterns;
	}
	
	public void deleteAt(int i) {
		if (patterns.length > 2) {
			Pattern[] newPatterns = new Pattern[patterns.length - 1];
			System.arraycopy(patterns, 0, newPatterns, 0, i);
			System.arraycopy(patterns, i + 1, newPatterns, i, patterns.length - i - 1);
			patterns = newPatterns;
		}
	}
	
	@SuppressWarnings("unused")
	public void dump() {
		for (int i=0; i < patterns.length; i++) {
			System.out.println("Pattern #"+i+" input: "+Arrays.toString(patterns[i].input)+" output: "+Arrays.toString(patterns[i].output));
		}
	}

}
