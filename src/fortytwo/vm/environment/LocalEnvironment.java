package fortytwo.vm.environment;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.Structure;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralObject;

public class LocalEnvironment {
	public final GlobalEnvironment global;
	public final VariableRoster vars;
	public LocalEnvironment(GlobalEnvironment global) {
		this.global = global;
		vars = new VariableRoster();
	}
	public static LocalEnvironment minimalEnvironment(GlobalEnvironment global) {
		return new LocalEnvironment(global);
	}
	public Expression referenceTo(VariableIdentifier id) {
		Expression localE = vars.referenceTo(id);
		if (localE != null) return localE;
		Expression globalE = global.vars.referenceTo(id);
		return globalE;
	}
	public LiteralExpression instance(ConcreteType type,
			VariableRoster fieldValues) {
		Structure struct = global.structs.referenceTo(type);
		if (struct == null) return null;
		return new LiteralObject(struct, fieldValues);
	}
}
