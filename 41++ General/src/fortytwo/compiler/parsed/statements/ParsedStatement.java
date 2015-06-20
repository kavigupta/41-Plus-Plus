package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.parsed.Sentence;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.statements.Statement;

public interface ParsedStatement extends Sentence {
	public Statement contextualize(StaticEnvironment env);
	/**
	 * This always returns true or throws an error. The return type is just to
	 * make checking explicit
	 */
	public boolean typeCheck(StaticEnvironment environment);
}
