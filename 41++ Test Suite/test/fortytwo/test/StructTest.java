package fortytwo.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fortytwo.compiler.Compiler42;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Parser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.LocalEnvironment;

public class StructTest {
	public static final String TEST_STRUCTS = "Define a type called singleton."
			+ "Define a type called record that contains a string called \"name\" and a number called \"dateOfBirth\"."
			+ "Define a type called triple of \"Tx\", \"Ty\", and \"Tz\" that contains a \"Tx\" called \"x\", a \"Ty\" called \"y\", and a \"Tz\" called \"z\"."
			+ "Define a type called pair of \"k\" and \"v\" that contains a \"k\" called \"key\" and a \"v\" called \"value\".";
	GlobalEnvironment env;
	@Before
	public void init() {
		env = Compiler42.compile(TEST_STRUCTS);
	}
	@Test
	public void singletonTest() {
		assertPrint("{singleton}\n",
				"Define a singleton called \"x\". Tell me what \"x\" is.");
		assertPrint("true\n",
				"Define a singleton called \"x\". Define a singleton called \"y\". Tell me what (\"x\" is equal to \"y\") is.");
	}
	@Test
	public void concreteStructTest() {
		assertPrint("{record: \"name\" <= '41++', \"dateOfBirth\" <= 2015}\n",
				"Define a record called \"lang\" with a \"name\" of '41++' and a \"dateOfBirth\" of 2015. Tell me what \"lang\" is.");
		assertPrint("false\n",
				"Define a record called \"lang\" with a \"name\" of '41++' and a \"dateOfBirth\" of 2015."
						+ "Define a record called \"lang_date_wrong\" with a \"name\" of '41++' and a \"dateOfBirth\" of 2014."
						+ "Tell me what (\"lang\" is equal to \"lang_date_wrong\") is.");
		assertPrint("true\n",
				"Define a record called \"lang\" with a \"name\" of '41++' and a \"dateOfBirth\" of 2015."
						+ "Define a record called \"langcopy\" with a \"name\" of '41++' and a \"dateOfBirth\" of 2015."
						+ "Tell me what (\"lang\" is equal to \"langcopy\") is.");
		assertPrint("{record: \"name\" <= '41++', \"dateOfBirth\" <= 2015}\n",
				"Define a record called \"lang\" with a \"name\" of '41++' and a \"dateOfBirth\" of 2015."
						+ "Define a record called \"langcopy\" with a value of \"lang\"."
						+ "Tell me what \"langcopy\" is.");
		assertPrint(
				"{record: \"name\" <= 'te+-s()t[]', \"dateOfBirth\" <= 2015}\n",
				"Define a record called \"lang\" with a \"name\" of '41++' and a \"dateOfBirth\" of 2015."
						+ "Set the \"name\" of \"lang\" to 'te+-s()t[]'."
						+ "Tell me what \"lang\" is.");
		assertPrint("'41++'\n",
				"Define a record called \"lang\" with a \"name\" of '41++' and a \"dateOfBirth\" of 2015."
						+ "Tell me what (the \"name\" of \"lang\") is.");
		assertPrint("2015\n",
				"Define a record called \"lang\" with a \"name\" of '41++' and a \"dateOfBirth\" of 2015."
						+ "Tell me what (the \"dateOfBirth\" of \"lang\") is.");
	}
	@Test
	public void genericStructureTest() {
		assertPrint(
				"{(triple of number, string, and bool): \"x\" <= 2, \"y\" <= 'abc', \"z\" <= true}\n",
				"Define a (triple of number, string, and bool) called \"st\" with a \"x\" of 2, a \"y\" of 'abc', and a \"z\" of true. Tell me what \"st\" is.");
		assertPrint("true\n",
				"" + "Define a (triple of number, string, and bool) called \"st\" with a \"x\" of 2, a \"y\" of 'abc', and a \"z\" of true."
						+ "Define a (triple of number, string, and bool) called \"st2\" with a \"x\" of 2, a \"y\" of 'abc', and a \"z\" of true."
						+ "Tell me what (\"st\" is equal to \"st2\") is.");
		assertPrint("false\n",
				"" + "Define a (triple of number, string, and bool) called \"st\" with a \"x\" of 2, a \"y\" of 'abc', and a \"z\" of true."
						+ "Define a (triple of number, string, and bool) called \"st2\" with a \"x\" of 12345, a \"y\" of 'abc', and a \"z\" of true."
						+ "Tell me what (\"st\" is equal to \"st2\") is.");
		assertPrint("false\n",
				"" + "Define a (triple of string, string, and bool) called \"st\" with a \"x\" of '2', a \"y\" of 'abc', and a \"z\" of true."
						+ "Define a (triple of number, string, and bool) called \"st2\" with a \"x\" of 12345, a \"y\" of 'abc', and a \"z\" of true."
						+ "Tell me what (\"st\" is equal to \"st2\") is.");
		assertPrint("true\n",
				"" + "Define a (triple of number, string, and bool) called \"st\" with a \"x\" of 2, a \"y\" of 'abc', and a \"z\" of true."
						+ "Define a (triple of number, string, and bool) called \"st2\" with a \"x\" of 12345, a \"y\" of 'def', and a \"z\" of false."
						+ "Set the \"x\" of \"st2\" to 2. Set the \"y\" of \"st2\" to 'abc'. Set the \"z\" of \"st2\" to true."
						+ "Tell me what (\"st\" is equal to \"st2\") is.");
	}
	@Test
	public void nestedModificationTest() {
		assertPrint(
				"{(pair of (pair of number and number) and number): \"key\" <= {(pair of number and number): \"key\" <= 0, \"value\" <= 1}, \"value\" <= 2}\n"
						+ "{(pair of number and number): \"key\" <= 0, \"value\" <= 1}\n"
						+ "{(pair of number and number): \"key\" <= 12345, \"value\" <= 1}\n"
						+ "12345\n",
				"Define a (pair of number and number) called \"par\" with a \"key\" of 0 and a \"value\" of 1.\n"
						+ "Define a (pair of (pair of number and number) and number) called \"trip\" with a \"key\" of \"par\" and a \"value\" of 2.\n"
						+ "Tell me what \"trip\" is.\n"
						+ "Tell me what (the \"key\" of \"trip\") is.\n"
						+ "Set the \"key\" of (the \"key\" of \"trip\") to 12345.\n"
						+ "Tell me what (the \"key\" of \"trip\") is.\n"
						+ "Tell me what (the \"key\" of (the \"key\" of \"trip\")) is.");
	}
	public void assertPrint(String result, String statement) {
		LocalEnvironment loc = env.minimalLocalEnvironment();
		Parser.parse(statement).stream()
				.forEach(x -> ((ParsedStatement) x).execute(loc));
		assertEquals(result, VirtualMachine.popMessage());
	}
	public void assertEquivalence(String result, String toEvaluate) {
		LocalEnvironment loc = env.minimalLocalEnvironment();
		assertEquals(result,
				ExpressionParser
						.parseExpression(Tokenizer
								.tokenize(LiteralToken.entire(toEvaluate)))
				.literalValue(loc).toSourceCode());
	}
}
