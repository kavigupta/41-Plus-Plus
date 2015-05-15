package fortytwo.compiler.language.statements;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Statement;

public class ParsedDefinition implements ParsedStatement {
	public final TypeIdentifier type;
	public final VariableIdentifier name;
	public final ArrayList<Pair<VariableIdentifier, ParsedExpression>> fields;
	public ParsedDefinition(TypeIdentifier type, VariableIdentifier name,
			ArrayList<Pair<VariableIdentifier, ParsedExpression>> fields) {
		this.type = type;
		this.name = name;
		this.fields = fields;
	}
	@Override
	public Statement contextualize(LocalEnvironment environment) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SentenceType type() {
		return SentenceType.DEFINITION;
	}
}
