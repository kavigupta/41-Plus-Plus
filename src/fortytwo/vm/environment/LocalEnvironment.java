package fortytwo.vm.environment;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.constructions.GenericStructure;
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
	public LiteralExpression instance(
			TypeIdentifier type,
			ArrayList<Pair<VariableIdentifier, LiteralExpression>> fieldValues) {
		GenericStructure struct = global.structs.referenceTo(type);
		if (struct == null) return null;
		return new LiteralObject(struct, fieldValues);
	}
}
