package fortytwo.compiler.parsed.constructions;

import java.util.HashMap;
import java.util.Map;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.environment.VariableRoster;

public class ParsedVariableRoster {
	public final HashMap<VariableIdentifier, ParsedExpression> pairs = new HashMap<>();
	public void add(VariableIdentifier id, ParsedExpression expr) {
		pairs.put(id, expr);
	}
	public Iterable<Map.Entry<VariableIdentifier, ParsedExpression>> entryIterator() {
		return pairs.entrySet();
	}
	public VariableRoster contextualize(StaticEnvironment env) {
		VariableRoster roster = new VariableRoster();
		pairs.entrySet()
				.stream()
				.forEach(p -> roster.assign(p.getKey(), p.getValue()
						.contextualize(env)));
		return roster;
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
		ParsedVariableRoster other = (ParsedVariableRoster) obj;
		if (pairs == null) {
			if (other.pairs != null) return false;
		} else if (!pairs.equals(other.pairs)) return false;
		return true;
	}
}
