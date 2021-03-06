package fortytwo.test.parser;

import static fortytwo.test.Utilities.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fortytwo.vm.errors.ErrorType;

@SuppressWarnings("static-method")
public class ParserTest {
	@Test
	public void expressionParserTest() {
		validateExprParse("\"x\"");
		validateExprParse("'2345'");
		validateExprParse("2345.345");
		validateExprParse("(40+3)");
		assertEquals("(40+(-3))", cdLoopExpr("(40+-3)"));
		validateExprParse("(40%3)");
		validateExprParse("(true and false)");
		validateExprParse(
				"((the \"n\" th element of (the 3 rd element of \"matrix\"))*23)");
		assertEquals("", cdLoopExpr("[2345.345]"));
	}
	@Test
	public void typeParserTest() {
		// validateTypeParse("number");
		// validateTypeParse("(array of number)");
		// validateTypeParse("(array of (array of number))");
		validateTypeParse(
				"(function that takes a number and outputs a number)");
		// validateTypeParse(
		// "(function that takes a number and a (function that takes a number)
		// and outputs a number)");
		// validateTypeParse(
		// "(function that takes a number, a string, and an element_of_type_3
		// and outputs a number)");
		// validateTypeParse("(function that takes a number)");
		// assertEquals("procedure", cdLoopType("function"));
		// validateTypeParse("(function that outputs a number)");
		// validateTypeParse("procedure");
	}
	@Test
	public void invalidExpressionParserTest() {
		assertParseError(ErrorType.SYNTAX,
				"~2 +~ is not a valid arithmetic expression", 0, 3, "2 +");
		assertParseError(ErrorType.SYNTAX,
				"~* 2~ is not a valid arithmetic expression", 0, 3, "* 2");
		assertParseError(ErrorType.SYNTAX,
				"~/ 2~ is not a valid arithmetic expression", 0, 3, "/ 2");
		assertParseError(ErrorType.SYNTAX,
				"~% 2~ is not a valid arithmetic expression", 0, 3, "% 2");
		assertParseError(ErrorType.SYNTAX,
				"~2 * * 3~ is not a valid arithmetic expression", 0, 7,
				"2 * * 3");
		assertParseError(ErrorType.SYNTAX,
				"~2 * * 3~ is not a valid arithmetic expression", 0, 6,
				"2 ** 3");
	}
	@Test
	public void definitionParserTest() {
		validateLineParse("Define a number called \"x\".");
		validateLineParse("Define a number called \"x\" with a value of 2.");
		validateLineParse(
				"Define a number called \"x\" with a value of ((\"y\"*\"x\")+2).");
		validateLineParse("Define an (array of number) called \"arr\".");
		validateLineParse(
				"Define an (array of number) called \"arr\" with a \"length\" of 2.");
		validateLineParse(
				"Define an (array of (array of number)) called \"arr\" with a \"length\" of 2.");
		assertEquals(
				"Define a matrix called \"mat\" with a \"height\" of \"width\" and a \"width\" of 3.",
				cdLoopLine(
						"Define a matrix called \"mat\" with a \"width\" of 3 and a \"height\" of \"width\"."));
		assertEquals(
				"Define a (triple of number, number, and number) called \"cuboid\" with a \"depth\" of 342.31234567898765432113243565432,"
						+ " a \"height\" of (the 3 rd element of \"arr\"), and a \"width\" of (1*(2+3)).",
				cdLoopLine(
						"Define a (triple of number, number, and number) called \"cuboid\" with a \"width\" of (1*(2+3)),"
								+ " a \"height\" of (the 3 rd element of \"arr\"), and a \"depth\" of 342.31234567898765432113243565432."));
	}
	@Test
	public void reassignmentParserTest() {
		validateLineParse("Set the value of \"x\" to \"y\".");
		validateLineParse("Set the \"length\" of \"array\" to \"y\".");
		validateLineParse(
				"Set the \"length\" of \"array\" to ((\"x\"*2234543212345)+(34*2)).");
	}
	@Test
	public void functionDefTest() {
		validateLineParse(
				"Define a function called f \"x\" that takes a number called \"x\" and outputs a number.");
		validateLineParse(
				"Define a function called the \"n\" th prime after \"x\" that takes a number called \"n\""
						+ " and a number called \"x\" and outputs a number.");
		assertEquals(
				"Define a function called the \"n\" th prime after \"x\" while logging \"str\" that takes a number called \"n\","
						+ " a number called \"x\", and a string called \"str\" and outputs a number.",
				cdLoopLine(
						"Define a function called the \"n\" th prime after \"x\" while logging \"str\" that takes a number called \"n\","
								+ " a string called \"str\", and a number called \"x\" and outputs a number."));
		// more esoteric problems
		validateLineParse(
				"Define a function called procedure \"x\" that takes a number called \"x\".");
		validateLineParse("Define a function called procedure.");
		validateLineParse("Define a function called pi that outputs a number.");
		validateLineParse(
				"Define a function called the sum of \"v1\" and \"v2\" that takes"
						+ " an (array of number) called \"v1\" and an (array of number) called \"v2\""
						+ " and outputs an (array of number).");
		validateLineParse(
				"Define a function called the product of \"v1\" and \"v2\" that takes"
						+ " an (array of number) called \"v1\" and an (array of number) called \"v2\""
						+ " and outputs an (array of (array of number)).");
	}
	@Test
	public void functionReturnTest() {
		validateLineParse("Exit the function.");
		validateLineParse(
				"Exit the function and output (((3*45)+((2-3)*4))+3).");
		validateLineParse("Exit the function and output (the \"n\" th prime).");
	}
	@Test
	public void structDefTest() {
		validateLineParse("Define a type called vector that"
				+ " contains a number called \"x\", a number called \"y\", and a number called \"z\".");
		validateLineParse(
				"Define a type called pair of \"k\" and \"v\" that contains a \"k\" called \"key\" and a \"v\" called \"value\".");
		validateLineParse("Define a type called pair of \"k\" and \"v\".");
		validateLineParse(
				"Define a type called triple of \"a\", \"b\", and \"c\" that contains an (array of \"a\") called \"arr\".");
		validateLineParse("Define a type called null.");
	}
	@Test
	public void funcCallTest() {
		validateLineParse("Tell me what (2*3) is.");
		assertEquals("(the \"n\" th element of \"arr\").",
				cdLoopLine("Run the \"n\" th element of \"arr\"."));
	}
	@Test
	public void ifTest() {
		validateLineParse(
				"If (\"x\" is greater than 0):\n\tSet the value of \"x\" to 1.");
		validateLineParse("If (\"x\" is greater than 0):\n"
				+ "\tIf (\"y\" is greater than 2):\n"
				+ "\t\tSet the value of \"x\" to 1.\n"
				+ "\tOtherwise:\n\t\tSet the value of \"y\" to 3.");
		assertEquals(
				"If (\"x\" is greater than 0):\n\tSet the value of \"x\" to 1.\nOtherwise:\n\tSet the value of \"x\" to 2.",
				cdLoopLine(
						"If (\"x\" is greater than 0): Set the value of \"x\" to 1. Otherwise: Set the value of \"x\" to 2."));
		assertEquals(
				"If (\"x\" is greater than 0):\n"
						+ "\tSet the value of \"x\" to 1.\n"
						+ "\tTell me what \"x\" is.\n" + "Otherwise:\n"
						+ "\tSet the value of \"x\" to 2.",
				cdLoopLine("If (\"x\" is greater than 0):\n"
						+ "\tSet the value of \"x\" to 1. Tell me what \"x\" is.\n"
						+ "Otherwise: Set the value of \"x\" to 2."));
	}
	@Test
	public void whileTest() {
		assertEquals(
				"While (\"i\" is at most 10):\n"
						+ "\tIf (i is greater than 2):\n"
						+ "\t\tPerform some function on \"i\".\n"
						+ "\t\tSet the value of \"i\" to (\"i\"+1).",
				cdLoopLine("While (\"i\" is at most 10):\n"
						+ "\tIf (i is greater than 2):\n"
						+ "\t\tPerform some function on \"i\".\n\t\tSet the value of \"i\" to (\"i\"+1)."));
	}
}
