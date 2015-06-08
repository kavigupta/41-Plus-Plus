package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lib.standard.collections.Pair;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public class VariableRoster<T extends Expression> {
	private final ArrayList<Pair<VariableIdentifier, Expression>> pairs = new ArrayList<>();
	public void assign(VariableIdentifier name, T express) {
		// a variable being assigned twice should never happen if typechecking
		// did its job
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
		// this should never happen if typechecking did its job.
	}
	public T referenceTo(VariableIdentifier id) {
		for (Pair<VariableIdentifier, Expression> entry : pairs) {
			@SuppressWarnings("unchecked")
			T val = (T) entry.value;
			if (entry.key.equals(id)) return val;
		}
		// this should never happen if typechecking did its job.
		return null;
	}
	public void redefine(VariableIdentifier name, T express) {
		for (int i = 0; i < pairs.size(); i++) {
			if (!pairs.get(i).key.equals(name)) continue;
			pairs.set(i, Pair.getInstance(name, express));
			return;
		}
		// this should never happen if typechecking was done.
	}
	public T value() {
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
	public VariableRoster<LiteralExpression> literalValue(
			LocalEnvironment local) {
		VariableRoster<LiteralExpression> roster = new VariableRoster<>();
		pairs.stream().forEach(
				p -> roster.assign(p.getKey(),
						p.getValue().literalValue(local)));
		return roster;
	}
	@SuppressWarnings("unchecked")
	public void forEach(Consumer<Pair<VariableIdentifier, T>> consumer) {
		pairs.forEach(x -> consumer.accept(Pair.getInstance(x.key,
				(T) x.value)));
	}
	public List<VariableIdentifier> variables() {
		return pairs.stream().map(Pair::getKey).collect(Collectors.toList());
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
		@SuppressWarnings("unchecked")
		VariableRoster<T> other = (VariableRoster<T>) obj;
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
