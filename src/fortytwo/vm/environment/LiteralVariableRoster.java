package fortytwo.vm.environment;

import java.util.HashMap;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.expressions.LiteralExpression;

public class LiteralVariableRoster {
	public final HashMap<VariableIdentifier, LiteralExpression> pairs = new HashMap<>();
	public void assign(VariableIdentifier name, LiteralExpression express) {
		if (pairs.containsKey(name))
			throw new RuntimeException(/* LOWPRI-E */);
		pairs.put(name, express);
	}
	public void deregister(VariableIdentifier name) {
		pairs.remove(name);
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
	@Override
	public LiteralVariableRoster clone() {
		LiteralVariableRoster other = new LiteralVariableRoster();
		other.pairs.putAll(pairs);
		return other;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pairs == null) ? 0 : pairs.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		LiteralVariableRoster other = (LiteralVariableRoster) obj;
		if (pairs == null) {
			if (other.pairs != null) return false;
		} else if (!pairs.equals(other.pairs)) return false;
		return true;
	}
}
