package demoProject;

import java.util.ArrayList;
import java.util.List;

public class HelloWorldTest extends HelloWorld {

	public static void main(String[] args) {
		testSimpleCalculate();
		testFindBracedRange();
		String str = "foo";
		changeString(str);
		log("str:" + str);
	}
	
	public static void testFindBracedRange() {
		String expr = "xxx((y(y)y))zzz"; // 4,11
		Integer start, end;
		List<Integer> startEnd = getEnclosedRange(expr);
		start = startEnd.get(0);
		end = startEnd.get(1);
		if (start != 4 || end != 11) {
			log("FAILED: start:" + start + ";end:" + end);
		} else {
			log("OK");
		}
	}
	
	//
	public static void changeString(String str) {
		str = "bar";
	}
	
	public static void testSimpleCalculate() {
		boolean tc1, tc2, tc3, tc4;
		tc1 = testSimpleCalculate_1();
		tc2 = testSimpleCalculate_2();
		tc3 = testCalculate_1();
		tc4 = testCalculate_2();
		log("tc1:" + tc1 + ";tc2:" + tc2 +
				";tc3:" + tc3 + "tc4:" + tc4); 
	}
	
	public static boolean testCalculate_1() {
		String expr_1 = "1+2*3/MAX(4,5)";
		Integer result = calculate(expr_1);
		if (result == 2) {
			return true;
		}
		log(expr_1 + "=" + result);
		return false;
	}
	
	public static boolean testCalculate_2() {
		String expr_1 = "1+2*31/((4+5)*6+MAX(7,8))-9+MIN(10,11)"; // -7
		Integer result = calculate(expr_1);
		if (result == 3) {
			return true;
		}
		log(expr_1 + "=" + result);
		return false;
	}
	
	public static boolean testSimpleCalculate_1() {
		List<Object> statement = new ArrayList<>();
		statement.add(1);
		statement.add('+');
		statement.add(2);
		statement.add('*');
		statement.add(3);
		Integer result = simpleCalculate(statement);
		if (result == 7) {
			return true;
		}
		return false;
	}
	
	public static boolean testSimpleCalculate_2() {
		List<Object> statement = new ArrayList<>();
		statement.add(2);
		statement.add('*');
		statement.add(3);
		statement.add('+');
		statement.add(4);
		statement.add('-');
		statement.add(5);
		Integer result = simpleCalculate(statement);
		if (result == 5) {
			return true;
		}
		return false;
	}
	

}
