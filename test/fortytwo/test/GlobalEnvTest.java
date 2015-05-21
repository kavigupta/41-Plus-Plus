package fortytwo.test;

import org.junit.Before;
import org.junit.Test;

import fortytwo.compiler.parsed.statements.ParsedFunctionCall;
import fortytwo.language.identifier.FunctionName;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralString;

public class GlobalEnvTest {
	GlobalEnvironment env;
	@Before
	public void init() {
		env = GlobalEnvironment.getDefaultEnvironment(
				StaticEnvironment.getDefault(), VirtualMachine.defaultVM);
	}
	@Test
	public void basicsTest() {
		assertEquals("", ParsedFunctionCall.getInstance(FunctionName
				.getInstance("", "as", "a", "list", "of", "letters"),
				new LiteralString("hello")));
	}
}
