package fortytwo.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fortytwo.vm.VirtualMachine;

public class GlobalEnvTest extends EnvironmentTester {
	@Before
	public void init() {
		loadEnvironment("");
	}
	@Test
	public void printTest() {
		exec("Tell me what 'Hello, World' is.");
		assertEquals("'Hello, World'\n", VirtualMachine.popMessage());
		exec("Tell me what 'Hello. World' is.");
		assertEquals("'Hello. World'\n", VirtualMachine.popMessage());
		exec("Tell me what 'a + b = c + d-e+f=g,dsad. asde.3452,2' is.");
		assertEquals("'a + b = c + d-e+f=g,dsad. asde.3452,2'\n",
				VirtualMachine.popMessage());
	}
	@Test
	public void compareTest() {
		assertEquivalence("true", "1 is equal to 1");
		assertEquivalence("false", "3 is equal to 1");
		assertEquivalence("true", "3 is greater than 1");
		assertEquivalence("false", "3 is less than 1");
		assertEquivalence("false", "3 is less than 3");
		assertEquivalence("false", "3 is greater than 3");
		assertEquivalence("true", "3 is less than or equal to 3");
		assertEquivalence("true", "3 is greater than or equal to 3");
		assertEquivalence("true", "3 is less than or equal to 4");
		assertEquivalence("false", "3 is greater than or equal to 4");
		assertEquivalence("true", "3 is at most 4");
		assertEquivalence("false", "3 is at least 4");
	}
	@Test
	public void logOpTest() {
		assertEquivalence("false", "true and false");
		assertEquivalence("false", "false and false");
		assertEquivalence("false", "not true");
		assertEquivalence("true", "not (true and false)");
	}
	@Test
	public void stringLetterSplitTest() {
		assertEquivalence("['h', 'e', 'l', 'l', 'o']",
				"'hello' split into individual letters");
		assertEquivalence("'world'",
				"the letters ('world' split into individual letters) combined to form a string");
	}
}
