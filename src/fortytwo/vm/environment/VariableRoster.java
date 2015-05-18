package fortytwo.vm.environment;

import java.util.HashMap;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.expressions.LiteralExpression;

public class VariableRoster {
	public final HashMap<VariableIdentifier, LiteralExpression> pairs = new HashMap<>();
	public void assign(VariableIdentifier name, LiteralExpression express) {
		if (pairs.containsKey(name))
			throw new RuntimeException(/* LOWPRI-E */);
		pairs.put(name, express);
	}
	public LiteralExpression referenceTo(VariableIdentifier id) {
		LiteralExpression le = pairs.get(id);
		if (le == null) throw new RuntimeException(/* LOWPRI-E */);
		return le;
	}
	public void redefine(VariableIdentifier name, LiteralExpression express) {
		if (!pairs.containsKey(name))
			throw new RuntimeException(/* LOWPRI-E */);
		pairs.put(name, express);
	}
	public LiteralExpression value() {
		return pairs.get(VariableIdentifier.VALUE);
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		LiteralExpression exp = pairs.get(name);
		return exp == null ? null : exp.resolveType();
	}
}
