package fortytwo.compiler.language.statements;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;

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
}
