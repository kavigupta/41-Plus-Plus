package fortytwo.vm.statements;

import fortytwo.vm.environment.LocalEnvironment;

public interface Statement {
	public void execute(LocalEnvironment environment);
}
