package Evaluation;

import java.util.Map;

//The Expr class is the superclass of all expressions.
//The idea is that they encode mathematical expressions
//that can be evaluated given some environment.
public abstract class Expr {
	public abstract double evaluate(Map<String, Double> env);

	//constants always return the same thing.
	public static class Constant extends Expr {
		double value;
		public Constant(double v) {
			value = v;
		}
		public double evaluate(Map<String, Double> env) {
			return value;
		}

		public String toString() {
			return "" + value;
		}

	}
	//variables look themselves up in the environment.
	public static class Var extends Expr{
		String str;
		public Var(String s) {
			str = s;
		}
		public double evaluate(Map<String, Double> env) {
			return env.get(str);
		}
		public String toString() {
			return str;
		}

	}
	//an operator acts on other expressions. Simple Expressions can
	//be combined into complex expressions with operators.
	//they have a name so they can be printed correctly.
	public abstract static class Operator extends Expr {
		protected Expr e1;
		protected Expr e2;
		protected String name;

		public String toString() {
			return "(" + e1.toString() + name + e2.toString() + ")";
		}
	}
	public static class Add extends Operator{
		public Add(Expr e1, Expr e2) {
			this.e1 = e1;
			this.e2 = e2;
			name = "+";
		}
		public double evaluate(Map<String, Double> env) {
			return e1.evaluate(env) + e2.evaluate(env);
		}
	}
	public static class Minus extends Operator{
		public Minus(Expr e1, Expr e2) {
			this.e1 = e1;
			this.e2 = e2;
			name = "-";
		}
		public double evaluate(Map<String, Double> env) {
			return e1.evaluate(env) - e2.evaluate(env);
		}
	}
	public static class Mult extends Operator{
		public Mult(Expr e1, Expr e2) {
			this.e1 = e1;
			this.e2 = e2;
			name = "*";
		}
		public double evaluate(Map<String, Double> env) {
			return e1.evaluate(env) * e2.evaluate(env);
		}
	}
	public static class Div extends Operator{
		public Div(Expr e1, Expr e2) {
			this.e1 = e1;
			this.e2 = e2;
			name = "/";
		}
		public double evaluate(Map<String, Double> env) {
			return e1.evaluate(env) / e2.evaluate(env);
		}
	}
}