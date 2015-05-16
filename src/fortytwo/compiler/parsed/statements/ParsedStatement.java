package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.parsed.sentences.Sentence;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Statement;

public interface ParsedStatement extends Sentence {
	public Statement contextualize(LocalEnvironment environment);
}
