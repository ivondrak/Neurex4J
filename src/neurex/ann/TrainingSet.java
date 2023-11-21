package neurex.ann;

import java.util.Arrays;

public class TrainingSet {
	
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
	
	public void addPattern(Pattern pattern) {
		Pattern[] newPatterns = Arrays.copyOf(patterns,patterns.length+1);
		newPatterns[patterns.length] = pattern;

	}
	
	public void deleteAt(int i) {
		Pattern[] newPatterns = new Pattern[patterns.length-1];
        System.arraycopy(patterns, 0, newPatterns, 0, i);
        System.arraycopy(patterns, i+1, newPatterns, i, patterns.length-i-1);
        patterns = newPatterns;
	}
	
	public void dump() {
		for (int i=0; i < patterns.length; i++) {
			System.out.println("Pattern #"+i+" input: "+Arrays.toString(patterns[i].input)+" output: "+Arrays.toString(patterns[i].output));
		}
	}

}
