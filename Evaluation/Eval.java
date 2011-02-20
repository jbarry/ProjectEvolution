package Evaluation;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import Evaluation.Expr.Add;
import Evaluation.Expr.Mult;
import Evaluation.Expr.Div;
import Evaluation.Expr.Minus;
import Evaluation.Expr.Constant;
import Evaluation.Expr.Var;


public class Eval{

	//this code takes a list of strings
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String expression = sc.nextLine();
		List<String> symlist = new ArrayList<String>();
		symlist.addAll(Arrays.asList(expression.split(" ")));
		for (int i = 0; i < symlist.size(); i++)
			System.out.print(symlist.get(i) + " ");
		System.out.println();

		Stack<Expr> stack = new Stack<Expr>();
		String current;
		Expr expr = null;
		//this is the evaluator for the postfix expression.
		//when you do this in your project, you will want to ignore operators
		//that cause a stack underflow. Here these operators will crash the program.
		while (!symlist.isEmpty()) {
			current = symlist.remove(0);//pop off first symbol.

			if      (current.equals("+")) {
				try{
					Expr left = stack.pop();
					Expr right = stack.pop();
					expr = new Add(left, right);
				} catch(Exception e) {

				}
			}
			else if (current.equals("-")) {
				try{
					Expr left = stack.pop();
					Expr right = stack.pop();
					expr = new Minus(left, right);
				} catch(Exception e) {

				}     
			}
			else if (current.equals("*")) {
				try{
					Expr left = stack.pop();
					Expr right = stack.pop();
					expr = new Mult(left, right);
				} catch(Exception e) {
				}
			}
			else if (current.equals("/")) {
				try{
					Expr left = stack.pop();
					Expr right = stack.pop();
					expr = new Div(left, right);
				} catch(Exception e) {

				}		
			}
			else {
				try {//try to turn it into a number.
					double d = Double.parseDouble(current);
					expr = new Constant(d);
				}
				catch (Exception e){//not a number, so must be a variable
					expr = new Var(current);
				}
			}
			stack.push(expr);//push the resulting expression back on the stack and loop.
		}
		//now the top of the stack has an expression object that is exactly the
		//expression typed in from the command line.
		Expr result = stack.pop();

		//for some environment of variables, which I will hardcode.
		Map<String, Double> environment = new HashMap<String, Double>();
		environment.put("x", 10.0);
		environment.put("y", 1.0);
		//we can now evaluate the expression with this environment:
		System.out.println("with bindings: ");
		for (String key : environment.keySet()) {
			System.out.println(key + "-> " + environment.get(key));
		}
		System.out.print("will evaluate to: ");
		System.out.println(result.evaluate(environment));
		System.out.println();

		//second evaluation, to show that we can use many environments.
		environment = new HashMap<String, Double>();
		environment.put("x", 2.0);
		environment.put("y", -1.0);
		System.out.println("and with bindings: ");
		for (String key : environment.keySet()) {
			System.out.println(key + "-> " + environment.get(key));
		}
		System.out.print("will evaluate to: ");
		System.out.println(result.evaluate(environment));
		System.out.println("and the expr object can be printed with toString:");
		System.out.println(result.toString());
	}
}

