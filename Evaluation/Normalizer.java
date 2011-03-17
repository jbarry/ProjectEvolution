package Evaluation;
import Interactive.Pair;

public class Normalizer {

	private Pair<Double, Double> fromRange;
	private Pair<Double, Double> toRange;

	public Normalizer(Pair<Double, Double> aFromRange,
			Pair<Double, Double> aToRange) {
		fromRange = aFromRange;
		toRange = aToRange;
	}

	public static void main(String[] args) {
	}

	public static Double normalize(Pair<Double, Double> aFromRange,
			Pair<Double, Double> aToRange) {
		
		return 0.0;
	}

	public Double normalize() {

		return 0.0;
	}

	public void setFromRange(Pair<Double, Double> aFromRange) {
		fromRange = aFromRange;
	}

	public Pair<Double, Double> getFromRange() {
		return fromRange;
	}

	public void setToRange(Pair<Double, Double> aToRange) {
		toRange = aToRange;
	}

	public Pair<Double, Double> getToRange() {
		return toRange;
	}
}
