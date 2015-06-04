package fortytwo.compiler;

import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parser.Parser;
import fortytwo.compiler.parser.StatementParser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;

public class Compiler42 {
	public static GlobalEnvironment compile(String text, VirtualMachine vm) {
		return GlobalEnvironment.interpret(Parser.parse(text), vm);
	}
	public static void execute(String text, VirtualMachine vm) {
		GlobalEnvironment env = GlobalEnvironment.interpret(
				Parser.parse(text), vm);
		try {
			ParsedStatement ps = ((ParsedStatement) StatementParser
					.parseStatement(Tokenizer.tokenize(
							Context.synthetic(), "This first.")));
			ps.contextualize(env.staticEnv).execute(
					env.minimalLocalEnvironment());
		} catch (Throwable t) {
			vm.displayLine("Error in processing input");
		}
	}
}
