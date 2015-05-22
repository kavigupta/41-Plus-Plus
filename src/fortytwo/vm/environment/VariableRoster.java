package fortytwo.vm.environment;

import java.util.HashMap;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.expressions.Expression;

public class VariableRoster {
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
		VariableRoster other = (VariableRoster) obj;
		if (pairs == null) {
			if (other.pairs != null) return false;
		} else if (!pairs.equals(other.pairs)) return false;
		return true;
	}
	public final HashMap<VariableIdentifier, Expression> pairs = new HashMap<>();
	public void assign(VariableIdentifier name, Expression express) {
		if (pairs.containsKey(name))
			throw new RuntimeException(/* LOWPRI-E */);
		pairs.put(name, express);
	}
	public Expression referenceTo(VariableIdentifier id) {
		Expression le = pairs.get(id);
		if (le == null) throw new RuntimeException(/* LOWPRI-E */);
		return le;
	}
	public void redefine(VariableIdentifier name, Expression express) {
		if (!pairs.containsKey(name))
			throw new RuntimeException(/* LOWPRI-E */);
		pairs.put(name, express);
	}
	public Expression value() {
		return pairs.get(VariableIdentifier.VALUE);
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		Expression exp = pairs.get(name);
		return exp == null ? null : exp.resolveType();
	}
	public LiteralVariableRoster literalValue(LocalEnvironment local) {
		LiteralVariableRoster roster = new LiteralVariableRoster();
		pairs.entrySet()
				.stream()
				.forEach(p -> roster.assign(p.getKey(), p.getValue()
						.literalValue(local)));
		return roster;
	}
}
