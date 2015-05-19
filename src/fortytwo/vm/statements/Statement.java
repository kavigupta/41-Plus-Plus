package fortytwo.vm.statements;

import fortytwo.vm.environment.LocalEnvironment;

public interface Statement {
	public void execute(LocalEnvironment environment);
	/**
	 * This always returns true or throws an error. The return type is just to
	 * make checking explicit
	 */
	public boolean typeCheck();
}
