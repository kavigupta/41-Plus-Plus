package fortytwo.vm.environment;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.expressions.Expression;

public class LocalEnvironment {
	public final GlobalEnvironment global;
	public final VariableRoster vars;
	public LocalEnvironment(GlobalEnvironment global) {
		this.global = global;
		vars = new VariableRoster();
	}
	public Expression referenceTo(VariableIdentifier id) {
		Expression localE = vars.referenceTo(id);
		if (localE != null) return localE;
		Expression globalE = global.vars.referenceTo(id);
		return globalE;
	}
}
