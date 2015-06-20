package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.parsed.Sentence;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;

public interface ParsedStatement extends Sentence {
	/**
	 * This always returns true or throws an error. The return type is just to
	 * make checking explicit
	 */
	public boolean typeCheck(StaticEnvironment environment);
	public void execute(LocalEnvironment environment);
	public void clean(LocalEnvironment environment);
}
