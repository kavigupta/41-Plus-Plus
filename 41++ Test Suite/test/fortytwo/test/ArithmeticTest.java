package fortytwo.test;

import static fortytwo.test.Utilities.assertEquivalence;

import org.junit.Before;
import org.junit.Test;

import fortytwo.vm.environment.UnorderedEnvironment;
import fortytwo.vm.environment.TypeEnvironment;

public class ArithmeticTest {
	UnorderedEnvironment env;
	@Before
	public void init() {
		env = UnorderedEnvironment
				.getDefaultEnvironment(TypeEnvironment.getDefault());
	}
	@Test
	public void basicsTest() {
		assertEquivalence(2, "1+1", env);
		assertEquivalence(.5, "1/2", env);
		assertEquivalence(1. / 3., "1/3", env);
		assertEquivalence(3 - 4, "3-4", env);
		assertEquivalence(1234567890098765432L / 3532234L,
				"1234567890098765432//3532234", env);
		assertEquivalence(1234567890098765432L % 3532234L,
				"1234567890098765432%3532234", env);
	}
	@Test
	public void orderOfOperations() {
		assertEquivalence(3 + 4 * 5, "3 + 4*5", env);
		assertEquivalence(.375, "1/2 * 3/4", env);
		assertEquivalence(2, "2/3//(1/3)", env);
		assertEquivalence(3 * 3 + 4 * (3 - 2), "3 * 3 + 4* (3-2)", env);
		assertEquivalence(0 - -64, "0 - - 64", env);
		assertEquivalence(0 + +64, "0 ++ 64", env);
		assertEquivalence(0 + -64, "0 + - 64", env);
		assertEquivalence(0 - +64, "0 - + 64", env);
		assertEquivalence(1234567890098765432L / 3532234L * (234 + 1234),
				"1234567890098765432//3532234*(234+1234)", env);
		assertEquivalence(3 * +4, "3*+4", env);
		assertEquivalence(3 * -4, "3*-4", env);
	}
}
