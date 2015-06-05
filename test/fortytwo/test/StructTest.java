package fortytwo.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fortytwo.compiler.Compiler42;
import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Parser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.LocalEnvironment;

public class StructTest {
	public static final String TEST_STRUCTS = "Define a type called singleton."
			+ "Define a type called record that contains a string called _name and a number called _dateOfBirth."
			+ "Define a type called triple of _Tx, _Ty, and _Tz that contains a _Tx called _x, a _Ty called _y, and a _Tz called _z.";
	GlobalEnvironment env;
	@Before
	public void init() {
		env = Compiler42.compile(TEST_STRUCTS);
	}
	@Test
	public void singletonTest() {
		assertPrint("{singleton}\n",
				"Define a singleton called _x. Tell me what _x is.");
		assertPrint(
				"true\n",
				"Define a singleton called _x. Define a singleton called _y. Tell me what (_x is equal to _y) is.");
	}
	@Test
	public void concreteStructTest() {
		assertPrint(
				"{record: _name <= '41++', _dateOfBirth <= 2015}\n",
				"Define a record called _lang with a _name of '41++' and a _dateOfBirth of 2015. Tell me what _lang is.");
		assertPrint(
				"false\n",
				"Define a record called _lang with a _name of '41++' and a _dateOfBirth of 2015."
						+ "Define a record called _lang_date_wrong with a _name of '41++' and a _dateOfBirth of 2014."
						+ "Tell me what (_lang is equal to _lang_date_wrong) is.");
		assertPrint(
				"true\n",
				"Define a record called _lang with a _name of '41++' and a _dateOfBirth of 2015."
						+ "Define a record called _langcopy with a _name of '41++' and a _dateOfBirth of 2015."
						+ "Tell me what (_lang is equal to _langcopy) is.");
		assertPrint(
				"{record: _name <= '41++', _dateOfBirth <= 2015}\n",
				"Define a record called _lang with a _name of '41++' and a _dateOfBirth of 2015."
						+ "Define a record called _langcopy with a value of _lang."
						+ "Tell me what _langcopy is.");
		assertPrint(
				"{record: _name <= 'te+-s()t[]', _dateOfBirth <= 2015}\n",
				"Define a record called _lang with a _name of '41++' and a _dateOfBirth of 2015."
						+ "Set the _name of _lang to 'te+-s()t[]'."
						+ "Tell me what _lang is.");
		assertPrint(
				"'41++'\n",
				"Define a record called _lang with a _name of '41++' and a _dateOfBirth of 2015."
						+ "Tell me what (the _name of _lang) is.");
		assertPrint(
				"2015\n",
				"Define a record called _lang with a _name of '41++' and a _dateOfBirth of 2015."
						+ "Tell me what (the _dateOfBirth of _lang) is.");
	}
	@Test
	public void genericStructureTest() {
		assertPrint(
				"{(triple of number, string, and bool): _x <= 2, _y <= 'abc', _z <= true}\n",
				"Define a (triple of number, string, and bool) called _st with a _x of 2, a _y of 'abc', and a _z of true. Tell me what _st is.");
		assertPrint(
				"true\n",
				""
						+ "Define a (triple of number, string, and bool) called _st with a _x of 2, a _y of 'abc', and a _z of true."
						+ "Define a (triple of number, string, and bool) called _st2 with a _x of 2, a _y of 'abc', and a _z of true."
						+ "Tell me what (_st is equal to _st2) is.");
		assertPrint(
				"false\n",
				""
						+ "Define a (triple of number, string, and bool) called _st with a _x of 2, a _y of 'abc', and a _z of true."
						+ "Define a (triple of number, string, and bool) called _st2 with a _x of 12345, a _y of 'abc', and a _z of true."
						+ "Tell me what (_st is equal to _st2) is.");
		assertPrint(
				"false\n",
				""
						+ "Define a (triple of string, string, and bool) called _st with a _x of '2', a _y of 'abc', and a _z of true."
						+ "Define a (triple of number, string, and bool) called _st2 with a _x of 12345, a _y of 'abc', and a _z of true."
						+ "Tell me what (_st is equal to _st2) is.");
		assertPrint(
				"true\n",
				""
						+ "Define a (triple of number, string, and bool) called _st with a _x of 2, a _y of 'abc', and a _z of true."
						+ "Define a (triple of number, string, and bool) called _st2 with a _x of 12345, a _y of 'def', and a _z of false."
						+ "Set the _x of _st2 to 2. Set the _y of _st2 to 'abc'. Set the _z of _st2 to true."
						+ "Tell me what (_st is equal to _st2) is.");
	}
	public void assertPrint(String result, String statement) {
		LocalEnvironment loc = env.minimalLocalEnvironment();
		Parser.parse(statement)
				.stream()
				.map(x -> ((ParsedStatement) x)
						.contextualize(env.staticEnv))
				.forEach(x -> x.execute(loc));
		assertEquals(result, VirtualMachine.popMessage());
	}
	public void assertEquivalence(String result, String toEvaluate) {
		LocalEnvironment loc = env.minimalLocalEnvironment();
		assertEquals(
				result,
				ExpressionParser
						.parseExpression(
								Tokenizer.tokenize(Context.synthetic(),
										toEvaluate))
						.contextualize(env.staticEnv).literalValue(loc)
						.toSourceCode());
	}
}
