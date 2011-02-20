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

	public Eval() {

	}

	public static void main(String[] args) {
		ArrayList<String> strL = new ArrayList<String>();
		strL.add("/");
		strL.add("y");
		strL.add("x");
		strL.add("y");
		strL.add("-");
		strL.add("x");
		strL.add("/");
		strL.add("+");
		strL.add("+");
		Eval.evaluation(strL);
	}

	//this code takes a list of strings
	public static Expr evaluation(ArrayList<String> symList) {
		// Scanner sc = new Scanner(System.in);
		// String expression = sc.nextLine();
		// symlist.addAll(Arrays.asList(expression.split(" ")));

		for (int i = 0; i < symList.size(); i++)
			System.out.print(symList.get(i) + " ");
		System.out.println();

		Stack<Expr> stack = new Stack<Expr>();
		String current;
		Expr expr = null;
		
		while(!symList.isEmpty()) {
			if(symList.get(0).equals("+")) symList.remove(0);
			else if(symList.get(0).equals("-")) symList.remove(0);
			else if(symList.get(0).equals("/")) symList.remove(0);
			else if(symList.get(0).equals("*")) symList.remove(0);
			else break;
		}
		//this is the evaluator for the postfix expression.
		//when you do this in your project, you will want to ignore operators
		//that cause a stack underflow. Here these operators will crash the program.
		theLoop: while (!symList.isEmpty()) {
			current = symList.remove(0);//pop off first symbol.
			boolean isOp = false;
			if (current.equals("+")) {
				isOp = true;
				if(stack.size() > 1) {
					try{
						Expr left = stack.pop();
						Expr right = stack.pop();
						expr = new Add(left, right);
						System.out.println("pushed Plus");
					} catch(Exception e) {
						System.out.println("No operators for operand");
						System.out.println(stack.size());
						continue theLoop;
					}
				}
			} else if (current.equals("-")) {
				isOp = true;
				if (stack.size() > 1) {
					try{
						Expr left = stack.pop();
						Expr right = stack.pop();
						expr = new Minus(left, right);
						System.out.println("pushed Minus");
					} catch(Exception e) {
						System.out.println("No operators for operand");
						continue theLoop;
					}
				}
			} else if (current.equals("*")) {
				isOp = true;
				if(stack.size() > 1) {
					try{
						Expr left = stack.pop();
						Expr right = stack.pop();
						expr = new Mult(left, right);
						System.out.println("pushed Mult");
					} catch(Exception e) {
						System.out.println("No operators for operand");
						continue theLoop;
					}
				}
			} else if (current.equals("/")) {
				isOp = true;
				if(stack.size() > 1) {
					try{
						Expr left = stack.pop();
						Expr right = stack.pop();
						expr = new Div(left, right);
						System.out.println("pushed Div");
					} catch(Exception e) {
						System.out.println("No operators for operand");
						continue theLoop;
					}
				}
			} else if (!isOp) {
				expr = new Var(current);
				System.out.println("pushed variable");
			}
			System.out.println(expr.toString());
			stack.push(expr);//push the resulting expression back on the stack and loop.
			System.out.println(stack.size());
		}

		return stack.pop();

		//now the top of the stack has an expression object that is exactly the
		//expression typed in from the command line.
		// Expr result = stack.pop();
		//
		// //for some environment of variables, which I will hardcode.
		// Map<String, Double> environment = new HashMap<String, Double>();
		// environment.put("x", 10.0);
		// environment.put("y", 1.0);
		// //we can now evaluate the expression with this environment:
		// System.out.println("with bindings: ");
		// for (String key : environment.keySet()) {
		// System.out.println(key + "-> " + environment.get(key));
		// }
		// System.out.print("will evaluate to: ");
		// System.out.println(result.evaluate(environment));
		// System.out.println();
		//
		// //second evaluation, to show that we can use many environments.
		// environment = new HashMap<String, Double>();
		// environment.put("x", 2.0);
		// environment.put("y", -1.0);
		// System.out.println("and with bindings: ");
		// for (String key : environment.keySet()) {
		// System.out.println(key + "-> " + environment.get(key));
		// }
		// System.out.print("will evaluate to: ");
		// System.out.println(result.evaluate(environment));
		// System.out.println("and the expr object can be printed with toString:");
		// System.out.println(result.toString());
	}
}

