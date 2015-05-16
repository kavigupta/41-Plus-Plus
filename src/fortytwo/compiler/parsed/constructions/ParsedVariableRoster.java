package fortytwo.compiler.parsed.constructions;

import lib.standard.collections.Pair;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableRoster;

public class ParsedVariableRoster {
	public void add(VariableIdentifier id, ParsedExpression expr) {
		// TODO Auto-generated method stub
	}
	public Iterable<Pair<VariableIdentifier, ParsedExpression>> entryIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	public VariableRoster contextualize(LocalEnvironment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
