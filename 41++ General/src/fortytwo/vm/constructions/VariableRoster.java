package fortytwo.vm.constructions;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.errors.DNEErrors;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * A class representing a roster of variables.
 */
public class VariableRoster<T extends Expression> {
	public final ArrayList<Pair<VariableIdentifier, T>> pairs = new ArrayList<>();
	/**
	 * ww public void add(VariableIdentifier id, T expr) {
	 * pairs.add(Pair.getInstance(id, expr));
	 * }
	 * /**
	 * 
	 * @return an iterator over the variables
	 */
	public Iterable<Pair<VariableIdentifier, T>> entryIterator() {
		return pairs;
	}
	/**
	 * @return the number of variables in this roster
	 */
	public int numberOfVariables() {
		return pairs.size();
	}
	public void assign(VariableIdentifier name, Object express) {
		// a variable being assigned twice should never happen if typechecking
		// did its job
		@SuppressWarnings("unchecked")
		T val = (T) express;
		pairs.add(Pair.<VariableIdentifier, T> of(name, val));
	}
	public boolean assigned(VariableIdentifier name) {
		for (Pair<VariableIdentifier, T> entry : pairs) {
			if (entry.getKey().equals(name)) return true;
		}
		return false;
	}
	public void deregister(VariableIdentifier name) {
		for (int i = 0; i < pairs.size(); i++) {
			if (!pairs.get(i).getKey().equals(name)) continue;
			pairs.remove(i);
			return;
		}
		// this should never happen if typechecking did its job.
	}
	public T value() {
		try {
			return referenceTo(VariableIdentifier.VALUE);
		} catch (Throwable t) {
			return null;
		}
	}
	public T referenceTo(VariableIdentifier id) {
		for (Pair<VariableIdentifier, T> entry : pairs) {
			T val = entry.getValue();
			if (entry.getKey().equals(id)) return val;
		}
		// this should never happen if typechecking did its job.
		return null;
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
		return "ParsedVariableRoster [pairs=" + pairs + "]";
	}
	public void redefine(VariableIdentifier name, LiteralExpression express) {
		for (int i = 0; i < pairs.size(); i++) {
			if (pairs.get(i).getKey().equals(name)) {
				@SuppressWarnings("unchecked")
				T ex = (T) express;
				pairs.set(i, Pair.<VariableIdentifier, T> of(name, ex));
				return;
			}
		}
		DNEErrors.variableDNE(name);
	}
	public VariableRoster<LiteralExpression> literalValue(
			LocalEnvironment env) {
		VariableRoster<LiteralExpression> roster = new VariableRoster<LiteralExpression>();
		this.pairs.forEach(
				x -> roster.assign(x.getKey(), x.getValue().literalValue(env)));
		return roster;
	}
}
