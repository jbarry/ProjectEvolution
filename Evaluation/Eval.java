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
import java.util.Random;
import java.util.Collections;
import static java.lang.System.out;

public class Eval{

	public Eval() {

	}

	//Main used for testing.
	public static void main(String[] args) {
		ArrayList<String> symbols = new ArrayList<String>();
		ArrayList<String> strL = new ArrayList<String>();
		symbols.add("/");
		symbols.add("y");
		symbols.add("x");
		symbols.add("y");
		symbols.add("-");
		symbols.add("x");
		symbols.add("/");
		symbols.add("+");
		symbols.add("+");
		Collections.shuffle(symbols);
		Random r = new Random();
		for (int i = 0; i < 9; i++) {
			out.print("To be evaluated ");
			for (int j = 0; j < 9; j++) {
				strL.add(symbols.get(r.nextInt(symbols.size())));
				out.print(strL.get(j) + " ");
			}
			out.println();
			out.println("Evaluation " + Eval.evaluation(strL).toString());
			out.println();
		}
	}

	//this code takes a list of strings
	public static Expr evaluation(ArrayList<String> symList) {
//		out.print("To be evaluated: ");
//		for (int i = 0; i < 9; i++) {
//			out.print(symList.get(i));
//		}
//		out.println();
		Stack<Expr> stack = new Stack<Expr>();
		String current;
		Expr expr = null;
		
		//Removes the leading operators in the list.
		while(!symList.isEmpty()) {
			if(symList.get(0).equals("+")) symList.remove(0);
			else if(symList.get(0).equals("-")) symList.remove(0);
			else if(symList.get(0).equals("/")) symList.remove(0);
			else if(symList.get(0).equals("*")) symList.remove(0);
			else break;
		}
		
		//If the symList contains only one var then return the variable as
		//a Var Expr. Iterate through a clone of the list and remove
		//all operators. Then check if the number of variables left is one.
		ArrayList<String> variable = (ArrayList<String>) symList.clone();
		loop: for (int i = 0; i < variable.size(); i++) {
			if(variable.get(i).equals("+")) {
				variable.remove(i);
				i--;
				continue loop;
			}
			else if(variable.get(i).equals("-")){
				variable.remove(i);
				i--;
				continue loop;
			}
			else if(variable.get(i).equals("/")){
				variable.remove(i);
				i--;
				continue loop;
			}
			else if(variable.get(i).equals("*")){
				variable.remove(i);
				i--;
				continue loop;
			}
		}
		if (variable.size() == 1) {
			return new Var(variable.get(0));
		}
		
		//this is the evaluator for the postfix expression.
		theLoop: while (!symList.isEmpty()) {
			current = symList.remove(0);//pop off first symbol.
			boolean isOp = false;
			if (current.equals("+")) {
//				out.println("reached");
				isOp = true;
				if(stack.size() > 1) {
					try{
						Expr left = stack.pop();
						Expr right = stack.pop();
						expr = new Add(left, right);
//						System.out.println("pushed Plus");
					} catch(Exception e) {
//						System.out.println("No operators for operand");
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
//						System.out.println("pushed Minus");
					} catch(Exception e) {
//						System.out.println("No operators for operand");
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
//						System.out.println("pushed Mult");
					} catch(Exception e) {
//						System.out.println("No operators for operand");
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
//						System.out.println("pushed Div");
					} catch(Exception e) {
//						System.out.println("No operators for operand");
						continue theLoop;
					}
				}
			} else if (!isOp) {
				expr = new Var(current);
//				System.out.println("pushed variable");
			}
//			System.out.println(expr.toString());
			if (expr != null)
				stack.push(expr);//push the resulting expression back on the stack and loop.
			expr = null;
		}
		
//		out.println("After eval: " + stack.peek() + "\n");
		return stack.pop();
	}
}

