package fortytwo.compiler.parsed.constructions;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.expressions.Expression;

public class ParsedVariableRoster {
	private final ArrayList<Pair<VariableIdentifier, ParsedExpression>> pairs = new ArrayList<>();
	public void add(VariableIdentifier id, ParsedExpression expr) {
		pairs.add(Pair.getInstance(id, expr));
	}
	public Iterable<Pair<VariableIdentifier, ParsedExpression>> entryIterator() {
		return pairs;
	}
	public VariableRoster<Expression> contextualize(StaticEnvironment env) {
		VariableRoster<Expression> roster = new VariableRoster<>();
		pairs.stream().forEach(
				p -> roster.assign(p.getKey().contextualize(env), p
						.getValue().contextualize(env)));
		return roster;
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
		ParsedVariableRoster other = (ParsedVariableRoster) obj;
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
