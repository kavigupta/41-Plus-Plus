package fortytwo.test;

import org.junit.Before;
import org.junit.Test;

public class ArithmeticTest extends EnvironmentTester {
	@Before
	public void init() {
		loadEnvironment("");
	}
	@Test
	public void basicsTest() {
		assertEquivalence(2, "1+1");
		assertEquivalence(.5, "1/2");
		assertEquivalence(1. / 3., "1/3");
		assertEquivalence(3 - 4, "3-4");
		assertEquivalence(1234567890098765432L / 3532234L,
				"1234567890098765432//3532234");
		assertEquivalence(1234567890098765432L % 3532234L,
				"1234567890098765432%3532234");
	}
	@Test
	public void orderOfOperations() {
		assertEquivalence(3 + 4 * 5, "3 + 4*5");
		assertEquivalence(.375, "1/2 * 3/4");
		assertEquivalence(2, "2/3//(1/3)");
		assertEquivalence(3 * 3 + 4 * (3 - 2), "3 * 3 + 4* (3-2)");
		assertEquivalence(0 - -64, "0 - - 64");
		assertEquivalence(0 + +64, "0 ++ 64");
		assertEquivalence(0 + -64, "0 + - 64");
		assertEquivalence(0 - +64, "0 - + 64");
		assertEquivalence(1234567890098765432L / 3532234L * (234 + 1234),
				"1234567890098765432//3532234*(234+1234)");
		assertEquivalence(3 * +4, "3*+4");
		assertEquivalence(3 * -4, "3*-4");
	}
}
