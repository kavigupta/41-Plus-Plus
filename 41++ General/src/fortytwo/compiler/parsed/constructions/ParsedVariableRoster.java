package fortytwo.compiler.parsed.constructions;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.errors.DNEErrors;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * A class representing a roster of variables.
 */
public class ParsedVariableRoster<T extends ParsedExpression> {
	public final ArrayList<Pair<VariableIdentifier, T>> pairs = new ArrayList<>();
	/**
	 * Registers the given Variable ID with the given parsed expression.
	 */
	public void add(VariableIdentifier id, T expr) {
		pairs.add(Pair.getInstance(id, expr));
	}
	/**
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
		pairs.add(Pair.getInstance(name, val));
	}
	public boolean assigned(VariableIdentifier name) {
		for (Pair<VariableIdentifier, T> entry : pairs) {
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
	public T value() {
		try {
			return referenceTo(VariableIdentifier.VALUE);
		} catch (Throwable t) {
			return null;
		}
	}
	public T referenceTo(VariableIdentifier id) {
		for (Pair<VariableIdentifier, T> entry : pairs) {
			T val = entry.value;
			if (entry.key.equals(id)) return val;
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
		ParsedVariableRoster<T> other = (ParsedVariableRoster<T>) obj;
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
			if (pairs.get(i).key.equals(name)) {
				@SuppressWarnings("unchecked")
				T ex = (T) express;
				pairs.set(i, Pair.<VariableIdentifier, T> getInstance(name,
						ex));
				return;
			}
		}
		DNEErrors.variableDNE(name);
	}
	public ParsedVariableRoster<LiteralExpression> literalValue(
			LocalEnvironment env) {
		ParsedVariableRoster<LiteralExpression> roster = new ParsedVariableRoster<LiteralExpression>();
		this.pairs.forEach(x -> roster.add(x.key, x.value.literalValue(env)));
		return roster;
	}
}
