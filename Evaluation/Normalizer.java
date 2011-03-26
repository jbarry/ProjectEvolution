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
//		Normalizer norm = new Normalizer(-600.0, 600.0, -50.0, 50.0);
		Normalizer norm1 = new Normalizer(-600.0, 600.0, -50.0, 50.0);
		Normalizer norm2 = new Normalizer(-15.0, 15.0, -3.0, 3.0);
		out.println(norm2.getFromRange().left() + " " + norm2.getFromRange().right());
		norm1.setTest(norm2, 76, 82);
		out.println(norm2.getFromRange().left() + " " + norm2.getFromRange().right());
//		for(int i = -600; i <= 600; i++)
//			out.println(norm.normalize(i));
//		if(7346278 < 754/0) {
//			out.println("y");
//		} 
//		if(340589 > 754/0) out.println("n");
	}

	private double from1;
	private double to1;
	private double from2;
	private double to2;
	private double preCalc;
	
	public Normalizer() {
		from1 = 0.0;
		to1 = 0.0;
		from2 = 0.0;
		to2 = 0.0;
	}
	public Normalizer(double a, double b,
			double c, double d) {
		from1 = a;
		to1 = b;
		from2 = c;
		to2 = d;
		preCalc = (d-c)/(b-a);
	}

	public static Double normalize(
			double a, double b,
			double c, double d, double x) {
		return 1 + (x-a)*(d-c)/(b-a);
	}

	public Double normalize(double x) {
		return 1 + (x-from1)*preCalc;
	}

	public void setFromRange(double a, double b) {
		from1 = a;
		to1 = b;
	}

	public Pair<Double, Double> getFromRange() {
		return new Pair<Double, Double>(from1, to1);
	}

	public void setToRange(double c, double d) {
		from2 = c;
		to2 = d;
	}

	public Pair<Double, Double> getToRange() {
		return new Pair<Double, Double>(from2, to2);
	}
	
	public void setTest(Normalizer norma, double a, double b) {
		norma.setFromRange(a, b);
	}
}
