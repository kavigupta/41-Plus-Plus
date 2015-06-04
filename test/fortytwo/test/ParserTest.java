package fortytwo.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fortytwo.compiler.Context;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Parser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.language.Resources;

public class ParserTest {
	@Test
	public void padderTest() {
		assertEquals("Hello ,  World", "Hello, World".replaceAll(
				Resources.PAD_FIND, Resources.PAD_REPLACE));
	}
	@Test
	public void expressionParserTest() {
		validateExprParse("_x");
		validateExprParse("'2345'");
		validateExprParse("2345.345");
		validateExprParse("(40+3)");
		validateExprParse("(40+-3)");
		validateExprParse("(40%3)");
		validateExprParse("(true and false)");
		validateExprParse("((the _n th element of (the 3 rd element of _matrix))*23)");
		assertEquals("", cdLoopExpr("[2345.345]"));
	}
	@Test
	public void definitionParserTest() {
		validateLineParse("Define a number called _x.");
		validateLineParse("Define a number called _x with a value of 2.");
		validateLineParse("Define a number called _x with a value of ((_y*_x)+2).");
		validateLineParse("Define an (array of number) called _arr.");
		validateLineParse("Define an (array of number) called _arr with a _length of 2.");
		validateLineParse("Define an (array of (array of number)) called _arr with a _length of 2.");
		validateLineParse("Define a matrix called _mat with a _width of 3 and a _height of _width.");
		validateLineParse("Define a (triple of number, number, and number) called _cuboid with a _width of (1*(2+3)),"
				+ " a _height of (the 3 rd element of _arr), and a _depth of 342.31234567898765432113243565432.");
	}
	@Test
	public void reassignmentParserTest() {
		validateLineParse("Set the value of _x to _y.");
		validateLineParse("Set the _length of _array to _y.");
		validateLineParse("Set the _length of _array to ((_x*2234543212345)+(34*2)).");
	}
	@Test
	public void functionDefTest() {
		validateLineParse("Define a function called f _x that takes a number called _x and outputs a number.");
		validateLineParse("Define a function called the _n th prime after _x that takes a number called _n"
				+ " and a number called _x and outputs a number.");
		assertEquals(
				"Define a function called the _n th prime after _x while logging _str that takes a number called _n,"
						+ " a number called _x, and a string called _str and outputs a number.",
				cdLoopLine("Define a function called the _n th prime after _x while logging _str that takes a number called _n,"
						+ " a string called _str, and a number called _x and outputs a number."));
		// more esoteric problems
		validateLineParse("Define a function called procedure _x that takes a number called _x.");
		validateLineParse("Define a function called procedure.");
		validateLineParse("Define a function called pi that outputs a number.");
		validateLineParse("Define a function called the sum of _v1 and _v2 that takes"
				+ " an (array of number) called _v1 and an (array of number) called _v2"
				+ " and outputs an (array of number).");
		validateLineParse("Define a function called the product of _v1 and _v2 that takes"
				+ " an (array of number) called _v1 and an (array of number) called _v2"
				+ " and outputs an (array of (array of number)).");
	}
	@Test
	public void functionReturnTest() {
		validateLineParse("Exit the function.");
		validateLineParse("Exit the function and output (((3*45)+((2-3)*4))+3).");
		validateLineParse("Exit the function and output (the _n th prime).");
	}
	@Test
	public void structDefTest() {
		validateLineParse("Define a type called vector that"
				+ " contains a number called _x, a number called _y, and a number called _z.");
		validateLineParse("Define a type called pair of _k and _v that contains a _k called _key and a _v called _value.");
		validateLineParse("Define a type called pair of _k and _v.");
		validateLineParse("Define a type called triple of _a, _b, and _c that contains an (array of _a) called _arr.");
		validateLineParse("Define a type called null.");
	}
	@Test
	public void funcCallTest() {
		validateLineParse("Tell me what (2*3) is.");
		assertEquals("(the _n th element of _arr).",
				cdLoopLine("Run the _n th element of _arr."));
	}
	@Test
	public void ifTest() {
		validateLineParse("If (_x is greater than 0): Set the value of _x to 1.");
		validateLineParse("If (_x is greater than 0): Do the following: If (_y is greater than 2):"
				+ " Set the value of _x to 1. That's all. Otherwise: Set the value of _y to 3.");
		validateLineParse("If (_x is greater than 0): Set the value of _x to 1. Otherwise: Set the value of _x to 2.");
		validateLineParse("If (_x is greater than 0): Do the following: Set the value of _x to 1. Tell me what _x is. That's all. Otherwise: Set the value of _x to 2.");
	}
	@Test
	public void whileTest() {
		assertEquals(
				"While (_i is at most 10): "
						+ "Do the following: If (i is greater than 2): "
						+ "Do the following: "
						+ "Perform some function on _i. Set the value of _i to (_i+1). "
						+ "That's all. That's all.",
				cdLoopLine("While (_i is at most 10): "
						+ "If (i is greater than 2): "
						+ "Do the following: "
						+ "Perform some function on _i. Set the value of _i to (_i+1). "
						+ "That's all."));
	}
	public static void validateLineParse(String line) {
		assertEquals(line, cdLoopLine(line));
	}
	public static void validateExprParse(String expr) {
		assertEquals(expr, cdLoopExpr(expr));
	}
	private static String cdLoopLine(String line) {
		return Parser.parse(line).get(0).toSourceCode() + ".";
	}
	private static String cdLoopExpr(String line) {
		return ExpressionParser.parseExpression(
				Tokenizer.tokenize(Context.synthetic(), line))
				.toSourceCode();
	}
}
