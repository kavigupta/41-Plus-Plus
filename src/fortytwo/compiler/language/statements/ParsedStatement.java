package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.sentences.Sentence;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Statement;

public interface ParsedStatement extends Sentence {
	public Statement contextualize(LocalEnvironment environment);
}
