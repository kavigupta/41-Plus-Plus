package fortytwo.ide.environment;

import java.util.function.Supplier;

import fortytwo.compiler.Compiler42;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Parser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.ide.gui.LineHistory;
import fortytwo.language.classification.SentenceKind;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.TypeEnvironment;
import fortytwo.vm.environment.UnorderedEnvironment;
import fortytwo.vm.errors.ParserErrors;

public class GUILinkedEnvironment {
	public LineHistory history;
	private OrderedEnvironment console;
	private final String last = "";
	private final Supplier<String> editor;
	public GUILinkedEnvironment(LineHistory history, Supplier<String> editor) {
		super();
		this.history = history;
		this.console = new OrderedEnvironment(UnorderedEnvironment
				.getDefaultEnvironment(TypeEnvironment.getDefault()));
		VirtualMachine.displayln = history::displayln;
		VirtualMachine.displayerr = error -> {
			history.displayerr(error);
			throw new RuntimeException("~~~has been logged");
		};
		this.editor = editor;
	}
	public void execute(String cmd) {
		if (cmd.trim().endsWith(".")) {
			for (final Sentence x : Parser.parse(cmd))
				if (x.kind().kind == SentenceKind.STATEMENT)
					((ParsedStatement) x).execute(console);
				else ParserErrors.expectedStatement(x);
		} else
			history.displayOutput(ExpressionParser
					.parseExpression(
							Tokenizer.tokenize(LiteralToken.entire(cmd)))
					.literalValue(console));
	}
	public void refresh() {
		final String newSource = editor.get();
		if (newSource.equals(last)) return;
		final UnorderedEnvironment newEnvironment = Compiler42
				.compile(newSource);
		console = console.reinitialize(newEnvironment);
	}
}
