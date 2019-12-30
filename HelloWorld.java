package demoProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelloWorld {

	public static void main(String[] args) {
		String expression = "1+2*3";
		Integer result = calculate(expression);
		log(expression + "=" + result);
	}
	
	public static Integer calculate(String expression) {
		List<Object> statement = new ArrayList<>();
		evaluate(expression, statement);
		return simpleCalculate(statement);
	}

    // calculate statement only has +-*/ operators.
	// no validate for statement.
	protected static Integer simpleCalculate(List<Object> statement) {
		if (statement.size() == 1) {
			return (Integer)statement.get(0);
		}
		
		if (statement.size() < 3) {
			log("Illegal statement: " + statement);
			return null;
		}
		
		// 1. calculate multiply and division first,
		//    just like a chain effect.
		for (int i = 1; i < statement.size(); i += 2) {
			char ch = (char)statement.get(i);
			Integer lop = (Integer)statement.get(i - 1);
			Integer rop = (Integer)statement.get(i + 1);
			if (ch == '*') {
				rop = lop * rop;
			} else if (ch == '/') {
				rop = lop / rop;
			} else {
				continue;
			}
			statement.set(i - 1, null);
			statement.set(i, null); // mark as deleted
			statement.set(i + 1, rop); // mark as deleted
			log("rop1:" + rop);
		}
		
		// remove deleted objects.
		statement.removeIf(obj -> obj == null);
		log("statement:" + statement);
		
		// 2. calculate plus and minus.	
		for (int i = 1; i < statement.size(); i += 2) {
			char ch = (char)statement.get(i);
			Integer lop = (Integer)statement.get(i - 1);
			Integer rop = (Integer)statement.get(i + 1);
			if (ch == '+') {
				rop = lop + rop;
			} else if (ch == '-') {
				rop = lop - rop;
			} else {
				log("unknown operator:" + ch);
				continue;
			}
			statement.set(i - 1, null);
			statement.set(i, null); // mark as deleted
			statement.set(i + 1, rop); // mark as deleted
			log("rop2:" + rop);
		}
		// remove deleted objects.
		statement.removeIf(obj -> obj == null);
		
		log("statement:" + statement);
		
		Integer lastOperand = (Integer)statement.get(0);
		return lastOperand;
	}
	
	// evaluate expression string into statement 
	// which consists number and operators.
	private static void evaluate(String expression, List<Object> statement) {
		
		expression = parseNumber(expression, statement);

		expression = parseOperator(expression, statement);
		
		expression = parseEnclosed(expression, statement);
		
		expression = parseMinMax(expression, statement);
		
		if (expression.startsWith(" ")) {
			evaluate(expression.substring(1, expression.length()), statement);
			return;
		} 
		
		if (expression.length() == 0) {
			return;
		}
		
		evaluate(expression, statement);
	}

	// return pair of start and end.
	protected static List<Integer> getEnclosedRange(String expression) {
		int openBraceCount = 0;
		int closeBraceCount = 0;
		Integer begin = 0;
		Integer end = 0;
		for (int i = 0; i < expression.length(); i++) {
			char ch = expression.charAt(i);
			if (ch == '(') {
				openBraceCount++;
				if (openBraceCount == 1) {
					begin = i + 1; // next char
				}
			}
			if (ch == ')') {
				closeBraceCount++;
			}
			if (openBraceCount == closeBraceCount && closeBraceCount > 0) {
				end = i; // exclusive
				break;
			}
		}
		
		return new ArrayList<Integer>(Arrays.asList(begin, end));
	}

	// evaluate immediately
	private static String parseMinMax(String expression, List<Object> statement) {
		if (expression.length() == 0) {
			return expression;
		}
		
		char operator;
		if (expression.startsWith("MIN(")) {
			operator = 'i';
		} else if (expression.startsWith("MAX(")) {
			operator = 'a';
		} else {
			return expression;
		}
		
		Integer begin, end;
		begin = end = 0;
		List<Integer> startEnd = getEnclosedRange(expression);
		begin = startEnd.get(0);
		end = startEnd.get(1);
		String subExpression = expression.substring(begin, end);
		Integer commaIndex = subExpression.indexOf(',');
		String leftOperandStr = subExpression.substring(0, commaIndex);
		String rightOperandStr = subExpression.substring(commaIndex + 1,
				subExpression.length());
		Integer leftOperand = calculate(leftOperandStr);
		Integer rightOperand = calculate(rightOperandStr);
		
		// evaluate right now
		if (operator == 'i') {
			leftOperand = Math.min(leftOperand, rightOperand);
		} else if (operator == 'a') {
			leftOperand = Math.max(leftOperand, rightOperand);
		}
		
		statement.add(leftOperand);
		return expression.substring(end + 1, expression.length());
	}

	private static String parseEnclosed(String expression, List<Object> statement) {
		if (expression.length() == 0) {
			return expression;
		}
		if (!expression.startsWith("(")) {
			return expression;
		}
		
		List<Integer> beginEndIndex = getEnclosedRange(expression);
		String subExpr = expression.substring(beginEndIndex.get(0), beginEndIndex.get(1));
		statement.add(calculate(subExpr));
		
		return expression.substring(beginEndIndex.get(1) + 1, expression.length());
	}

	private static String parseOperator(String expression, List<Object> statement) {
		if (expression.length() == 0) {
			return expression;
		}
		String operators = "+-*/";
		char ch = expression.charAt(0);
		if (!operators.contains(ch + "")) {
			return expression;
		}
		
		statement.add(ch);
		String subExpr = expression.substring(1, expression.length());
		return subExpr;
	}

	private static String digits = "0123456789";

	private static String parseNumber(String expression, List<Object> statement) {
		if (expression.length() == 0) {
			return expression;
		}
		char ch = expression.charAt(0);
		if (!digits.contains(ch + "")) {
			return expression;
		}
		int end = 1;
		for (int i = 0; i < expression.length(); i++) {
			ch = expression.charAt(i);
			if (!digits.contains(ch + "")) {
				break; //end = i; // exclusive
			}
			end = i + 1;
		}
		String numberStr = expression.substring(0, end);
		Integer number = Integer.parseInt(numberStr);

		statement.add(number);
		String subExpr = expression.substring(end, expression.length());
		return subExpr;
	}
	
	public static boolean debug = true;
	public static void log(String info) {
		if (debug) {
			System.out.println(info);
		}
	}

}
