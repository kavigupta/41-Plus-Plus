package fortytwo.compiler;

import java.util.Arrays;

import fortytwo.compiler.parsed.statements.ParsedFunctionCall;
import fortytwo.compiler.parser.Parser;
import fortytwo.language.identifier.FunctionName;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;
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
	 * @return a global environment representing the source code given
	 */
	public static GlobalEnvironment compile(String text) {
		return GlobalEnvironment.interpret(Parser.parse(text));
	}
	/**
	 * @param text
	 *        41++ code to execute
	 */
	public static void execute(String text) {
		final GlobalEnvironment env = GlobalEnvironment
				.interpret(Parser.parse(text));
		try {
			final ParsedFunctionCall pfc = ParsedFunctionCall.getInstance(
					FunctionName.getInstance("This", "first"), Arrays.asList());
			pfc.isTypeChecked(env.staticEnv);
			pfc.execute(env.minimalLocalEnvironment());
		} catch (final Throwable t) {
			VirtualMachine.error(ErrorType.PARSING, "Main method not found.",
					Context.entire(text));
		}
	}
}
