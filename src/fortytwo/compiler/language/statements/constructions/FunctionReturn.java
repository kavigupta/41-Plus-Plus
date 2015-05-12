package fortytwo.compiler.language.statements.constructions;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.statements.ParsedStatement;

public class FunctionReturn implements ParsedStatement {
	public final ParsedExpression output;
	public FunctionReturn(ParsedExpression output) {
		this.output = output;
	}
}
