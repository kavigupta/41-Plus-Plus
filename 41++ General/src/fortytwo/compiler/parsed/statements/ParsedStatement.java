package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.parsed.Sentence;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;

public abstract class ParsedStatement implements Sentence {
	private boolean isTypeChecked = false;
	public boolean checkType(StaticEnvironment environment) {
		if (!isTypeChecked) isTypeChecked = typeCheck1(environment);
		return isTypeChecked;
	}
	/**
	 * This always returns true or throws an error. The return type is just to
	 * make checking explicit
	 */
	protected abstract boolean typeCheck1(StaticEnvironment environment);
	public abstract void execute(LocalEnvironment environment);
	public abstract void clean(LocalEnvironment environment);
}
