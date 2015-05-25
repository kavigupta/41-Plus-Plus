package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.function.Consumer;

import lib.standard.collections.Pair;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public class VariableRoster {
	private final ArrayList<Pair<VariableIdentifier, Expression>> pairs = new ArrayList<>();
	public void assign(VariableIdentifier name, Expression express) {
		if (assigned(name)) throw new RuntimeException(/* LOWPRI-E */);
		pairs.add(Pair.getInstance(name, express));
	}
	public boolean assigned(VariableIdentifier name) {
		for (Pair<VariableIdentifier, Expression> entry : pairs) {
			if (entry.key.equals(name)) return true;
		}
		return false;
	}
	public void deregister(VariableIdentifier name) {
		for (int i = 0; i < pairs.size(); i++) {
			if (!pairs.get(i).key.equals(name)) continue;
			pairs.remove(i);
			return;
		}
		throw new RuntimeException(/* LOWPRI-E */);
	}
	public Expression referenceTo(VariableIdentifier id) {
		for (Pair<VariableIdentifier, Expression> entry : pairs) {
			if (entry.key.equals(id)) return entry.value;
		}
		throw new RuntimeException(/* LOWPRI-E */id.name + "\t" + this);
	}
	public void redefine(VariableIdentifier name, LiteralExpression express) {
		for (int i = 0; i < pairs.size(); i++) {
			if (!pairs.get(i).key.equals(name)) continue;
			pairs.set(i, Pair.getInstance(name, express));
			return;
		}
		throw new RuntimeException(/* LOWPRI-E */);
	}
	public Expression value() {
		try {
			return referenceTo(VariableIdentifier.VALUE);
		} catch (Throwable t) {
			return null;
		}
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		try {
			return referenceTo(name).resolveType();
		} catch (Throwable t) {
			return null;
		}
	}
	public LiteralVariableRoster literalValue(LocalEnvironment local) {
		LiteralVariableRoster roster = new LiteralVariableRoster();
		pairs.stream().forEach(
				p -> roster.assign(p.getKey(),
						p.getValue().literalValue(local)));
		return roster;
	}
	public void forEach(Consumer<Pair<VariableIdentifier, Expression>> consumer) {
		pairs.forEach(consumer);
	}
	public int size() {
		return pairs.size();
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
		VariableRoster other = (VariableRoster) obj;
		if (pairs == null) {
			if (other.pairs != null) return false;
		} else if (!pairs.equals(other.pairs)) return false;
		return true;
	}
	@Override
	public String toString() {
		return "VariableRoster [pairs=" + pairs + "]";
	}
}
