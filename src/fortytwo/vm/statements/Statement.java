package fortytwo.vm.statements;

import fortytwo.language.ParsedConstruct;
import fortytwo.vm.environment.LocalEnvironment;

public interface Statement extends ParsedConstruct {
	public void execute(LocalEnvironment environment);
	public void clean(LocalEnvironment environment);
	/**
	 * This always returns true or throws an error. The return type is just to
	 * make checking explicit
	 */
	public boolean typeCheck();
}
