package fortytwo.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import fortytwo.compiler.parser.Parser;

public class ParserTest {
	@Test
	public void tokenizerTest() {
		assertEquals(
				Arrays.asList("Define", "a", "number", "called", "_x", "."),
				Parser.tokenize42("Define a number called _x."));
		assertEquals(
				Arrays.asList("If", "4.0", "+", "4.0", "=", "2", ":",
						"Dothis", ";", "otherwise", ":", "Dothat", "."),
				Parser.tokenize42("If 4.0 + 4.0 = 2: Dothis; otherwise: Dothat."));
		assertEquals(
				Arrays.asList("Define", "a", "string", "called", "_x",
						"with", "a", "value", "of", "'()[]'\r\n'", "."),
				Parser.tokenize42("Define a string called _x with a value of '()[]\\'\\r\\n'."));
		assertEquals(
				Arrays.asList("Define", "a", "number", "called", "_x", "."),
				Parser.tokenize42("Define a [COMPILER ERROR HERE ''''''''''''] number called _x."));
		assertEquals(
				Arrays.asList("Set", "the", "value", "of", "_x", "to",
						"((1 % 2) + (2 + 3) * (3 // 4))", "."),
				Parser.tokenize42("Set the value of _x to ((1%2)+(2+3)*(3//4))."));
		assertEquals(
				Arrays.asList("_technically_this_is_a_valid_var''iable_name'"),
				Parser.tokenize42("_technically_this_is_a_valid_var''iable_name'"));
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
				cdLoop("Define a function called the _n th prime after _x while logging _str that takes a number called _n,"
						+ " a string called _str, and a number called _x and outputs a number."));
		// more esoteric problems
		validateLineParse("Define a function called procedure _x that takes a number called _x.");
	}
	public static void validateLineParse(String line) {
		assertEquals(line, cdLoop(line));
	}
	private static String cdLoop(String line) {
		return Parser.parse(line).get(0).toSourceCode() + ".";
	}
}
