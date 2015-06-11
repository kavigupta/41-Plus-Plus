package fortytwo.ide.environment;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Parser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.ide.gui.LineHistory;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.ParserErrors;

public class GUILinkedEnvironment {
	public LineHistory history;
	private LocalEnvironment env;
	public GUILinkedEnvironment(LineHistory history) {
		super();
		this.history = history;
		this.env = new LocalEnvironment(
				GlobalEnvironment.getDefaultEnvironment(StaticEnvironment
						.getDefault()));
		VirtualMachine.displayln = history::displayln;
		VirtualMachine.displayerr = err -> {
			history.displayerr(err);
			throw new RuntimeException();
		};
	}
	public void execute(String cmd) {
		if (cmd.endsWith(".")) {
			Parser.parse(cmd).forEach(
					x -> {
						if (x instanceof ParsedStatement) {
							((ParsedStatement) x).contextualize(
									env.staticEnvironment()).execute(
									env);
						} else {
							ParserErrors.expectedStatement(x);
						}
					});
		} else {
			history.displayOutput(ExpressionParser
					.parseExpression(
							Tokenizer.tokenize(Context.minimal(cmd), cmd))
					.contextualize(env.staticEnvironment())
					.literalValue(env));
		}
	}
}
