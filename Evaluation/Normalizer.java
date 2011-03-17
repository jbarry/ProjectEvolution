package Evaluation;
import Interactive.Pair;
import static java.lang.System.out;

/**
*This class can be instantiated or we
*can use that static method normalize().
*This class can be used more efficiently if
*it is instantiated.
*/
public class Normalizer {
	
	public static void main(String[] args) {
//		for(int i = 1; i < 167; i++) {
//			out.println(Normalizer.normalize(
//					new Pair<Double, Double> (1.0, 167.0), 
//					new Pair<Double, Double> (1.0, 53.0), i));
//		}
//		Normalizer norm = new Normalizer
//		(new Pair<Double, Double> (1.0, 167.0),
//				new Pair<Double, Double> (1.0, 53.0));
//		for(int i = 1; i < 167; i++) {
//			out.println(norm.normalize(i));
//		}
	}

	private Pair<Double, Double> fromRange;
	private Pair<Double, Double> toRange;
	private double preCalc;
	
	public Normalizer() {
		fromRange = null;
		toRange = null;
	}
	public Normalizer(Pair<Double, Double> aFromRange,
			Pair<Double, Double> aToRange) {
		fromRange = aFromRange;
		toRange = aToRange;
		double a = fromRange.left();
		double b = fromRange.right();
		double c = toRange.left();
		double d = toRange.right();
		preCalc = (d-c)/(b-a);
	}

	public static Double normalize(Pair<Double, Double> aFromRange,
			Pair<Double, Double> aToRange, double x) {
		double a = aFromRange.left();
		double b = aFromRange.right();
		double c = aToRange.left();
		double d = aToRange.right();
		return 1 + (x-a)*(d-c)/(b-a);
	}

	public Double normalize(double x) {
		double a = fromRange.left();
		return 1 + (x-a)*preCalc;
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
