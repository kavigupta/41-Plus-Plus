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
}
