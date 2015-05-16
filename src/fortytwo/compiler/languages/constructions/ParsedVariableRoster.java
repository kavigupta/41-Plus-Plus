package fortytwo.compiler.languages.constructions;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.VariableIdentifier;
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
