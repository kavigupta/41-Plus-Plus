package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.VariableIdentifier;

public class ParsedAssignment implements ParsedStatement {
	public final VariableIdentifier name, field;
	public final ParsedExpression parseExpression;
	public ParsedAssignment(VariableIdentifier name, VariableIdentifier field,
			ParsedExpression parseExpression) {
		this.name = name;
		this.field = field;
		this.parseExpression = parseExpression;
	}
}
