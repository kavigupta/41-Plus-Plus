package fortytwo.vm.environment;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public class LocalEnvironment {
	public final GlobalEnvironment global;
	public LocalEnvironment(GlobalEnvironment global) {
		this.global = global;
	}
	public static LocalEnvironment minimalEnvironment(
			GlobalEnvironment globalEnvironment) {
		// TODO Auto-generated method stub
		return null;
	}
	public Expression referenceTo(VariableIdentifier id) {
		// TODO Auto-generated method stub
		return null;
	}
	public LiteralExpression instance(
			TypeIdentifier type,
			ArrayList<Pair<VariableIdentifier, LiteralExpression>> fieldValues) {
		// TODO Auto-generated method stub
		return null;
	}
}
