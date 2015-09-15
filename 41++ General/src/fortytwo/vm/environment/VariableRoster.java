package fortytwo.vm.environment;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.errors.DNEErrors;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * A class representing a roster of variables.
 */
public class VariableRoster<T extends Expression> {
	public final Map<VariableIdentifier, T> pairs = new TreeMap<>();
	/**
	 * ww public void add(VariableIdentifier id, T expr) {
	 * pairs.add(Pair.getInstance(id, expr));
	 * }
	 * /**
	 * 
	 * @return an iterator over the variables
	 */
	public Iterable<Entry<VariableIdentifier, T>> entryIterator() {
		return pairs.entrySet();
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
		final T val = (T) express;
		pairs.put(name, val);
	}
	public boolean assigned(VariableIdentifier name) {
		return pairs.containsKey(name);
	}
	public void deregister(VariableIdentifier name) {
		pairs.remove(name);
	}
	public T value() {
		try {
			return referenceTo(VariableIdentifier.VALUE);
		} catch (final Throwable t) {
			return null;
		}
	}
	public T referenceTo(VariableIdentifier id) {
		return pairs.get(id);
	}
	public void redefine(VariableIdentifier name, LiteralExpression express) {
		if (!assigned(name)) DNEErrors.variableDNE(name);
		this.assign(name, express);
	}
	public VariableRoster<LiteralExpression> literalValue(
			OrderedEnvironment env) {
		final VariableRoster<LiteralExpression> roster = new VariableRoster<LiteralExpression>();
		this.pairs.forEach((k, v) -> roster.assign(k, v.literalValue(env)));
		return roster;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (pairs == null ? 0 : pairs.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		@SuppressWarnings("unchecked")
		final VariableRoster<T> other = (VariableRoster<T>) obj;
		if (pairs == null) {
			if (other.pairs != null) return false;
		} else if (!pairs.equals(other.pairs)) return false;
		return true;
	}
	@Override
	public String toString() {
		return "ParsedVariableRoster [pairs=" + pairs + "]";
	}
}
