package fortytwo.ide.environment;

import java.util.function.Supplier;

import fortytwo.compiler.Compiler42;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.statements.Statement;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Parser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.ide.gui.LineHistory;
import fortytwo.language.classification.SentenceKind;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.UnorderedEnvironment;
import fortytwo.vm.environment.type.TopTypeEnvironment;
import fortytwo.vm.errors.ParserErrors;

public class GUILinkedEnvironment {
	public LineHistory history;
	private OrderedEnvironment console;
	private String last = "";
	private Supplier<String> editor;
	public GUILinkedEnvironment(LineHistory history, Supplier<String> editor) {
		super();
		this.history = history;
		this.console = new OrderedEnvironment(UnorderedEnvironment
				.getDefaultEnvironment(new TopTypeEnvironment()));
		VirtualMachine.displayln = history::displayln;
		VirtualMachine.displayerr = error -> {
			history.displayerr(error);
			throw new RuntimeException("~~~has been logged");
		};
		this.editor = editor;
	}
	public void execute(String cmd) {
		if (cmd.trim().endsWith(".")) {
			for (Sentence x : Parser.parse(cmd)) {
				if (x.kind().kind == SentenceKind.STATEMENT) {
					((Statement) x).execute(console);
				} else {
					ParserErrors.expectedStatement(x);
				}
			}
		} else {
			history.displayOutput(ExpressionParser
					.parseExpression(
							Tokenizer.tokenize(LiteralToken.entire(cmd)))
					.literalValue(console));
		}
	}
	public void refresh() {
		String newSource = editor.get();
		if (newSource.equals(last)) return;
		UnorderedEnvironment newEnvironment = Compiler42.compile(newSource);
		console = console.reinitialize(newEnvironment);
	}
}
