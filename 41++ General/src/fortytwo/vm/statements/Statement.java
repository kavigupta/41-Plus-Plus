package fortytwo.vm.statements;

import fortytwo.compiler.parsed.ParsedConstruct;
import fortytwo.vm.environment.LocalEnvironment;

public interface Statement extends ParsedConstruct {
	public void execute(LocalEnvironment environment);
	public void clean(LocalEnvironment environment);
}
