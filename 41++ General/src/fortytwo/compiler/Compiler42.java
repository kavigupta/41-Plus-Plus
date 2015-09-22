package fortytwo.compiler;

import java.util.Arrays;

import fortytwo.compiler.parsed.statements.FunctionCall;
import fortytwo.compiler.parser.Parser;
import fortytwo.language.identifier.FunctionName;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.UnorderedEnvironment;
import fortytwo.vm.errors.ErrorType;

/**
 * A utility class for generating and executing environments (and potentially
 * jvm bytecode in the future) from 41++ code.
 */
public class Compiler42 {
	/**
	 * Compiles the given text
	 * 
	 * @param text
	 *        41++ source code to be compiled
	 * @return an environment representing the source code given
	 */
	public static UnorderedEnvironment compile(String text) {
		return UnorderedEnvironment.interpret(Parser.parse(text));
	}
	/**
	 * @param text
	 *        41++ code to execute
	 */
	public static void execute(String text) {
		final UnorderedEnvironment env = UnorderedEnvironment
				.interpret(Parser.parse(text));
		try {
			final FunctionCall pfc = FunctionCall.getInstance(
					FunctionName.getInstance("This", "first"), Arrays.asList());
			pfc.isTypeChecked(env.typeEnv);
			pfc.execute(env.minimalLocalEnvironment());
		} catch (final Throwable t) {
			VirtualMachine.error(ErrorType.PARSING, "Main method not found.",
					Context.entire(text));
		}
	}
}
